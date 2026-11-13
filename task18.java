package task18;

import java.util.*;

record StageResult(boolean success, String stageName, String message) {}

class DeploymentReport {
    private final boolean overallSuccess;
    private final List<StageResult> executedStages;
    private final String activeServerVersion;

    public DeploymentReport(boolean overallSuccess, List<StageResult> executedStages, String activeServerVersion) {
        this.overallSuccess = overallSuccess;
        this.executedStages = executedStages;
        this.activeServerVersion = activeServerVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== Deployment Report ===\n");
        sb.append("Status: ").append(overallSuccess ? "SUCCESS" : "FAILED (Rolled back / Retained previous)").append("\n");
        for (StageResult r : executedStages) {
            sb.append(String.format(" - [%s] %s: %s%n", r.success() ? "PASS" : "FAIL", r.stageName(), r.message()));
        }
        sb.append("Live Server Version: ").append(activeServerVersion).append("\n");
        return sb.toString();
    }
}

class GitRepoFetcher {
    public StageResult fetch(String branch) {
        return new StageResult(true, "GitFetch", "Checked out latest commit from '" + branch + "'");
    }
}

class CompilerService {
    public StageResult compile(String branch) {
        if (branch.contains("broken-build")) {
            return new StageResult(false, "Compile", "Syntax error in MainController.java:42");
        }
        return new StageResult(true, "Compile", "Bytecode compiled with 0 errors");
    }
}

class UnitTestRunner {
    public StageResult runTests(String branch) {
        if (branch.contains("failing-test")) {
            return new StageResult(false, "UnitTests", "PaymentServiceTest failed (expected 200, got 500)");
        }
        return new StageResult(true, "UnitTests", "48/48 unit tests passed");
    }
}

class ArtifactPackager {
    public StageResult packageJar(String branch) {
        return new StageResult(true, "Package", "Built app-" + branch + ".jar");
    }
}

class StagingServer {
    private String currentVersion = "v1.4.2-stable";

    public StageResult deploy(String newVersion) {
        this.currentVersion = newVersion;
        return new StageResult(true, "Deploy", "Switched traffic to " + newVersion);
    }

    public String getCurrentVersion() {
        return currentVersion;
    }
}

class DeploymentFacade {
    private final GitRepoFetcher git;
    private final CompilerService compiler;
    private final UnitTestRunner tests;
    private final ArtifactPackager packager;
    private final StagingServer server;

    public DeploymentFacade(GitRepoFetcher git, CompilerService compiler, UnitTestRunner tests,
                            ArtifactPackager packager, StagingServer server) {
        this.git = git;
        this.compiler = compiler;
        this.tests = tests;
        this.packager = packager;
        this.server = server;
    }

    public DeploymentReport deployBuild(String branchName) {
        List<StageResult> history = new ArrayList<>();

        StageResult s1 = git.fetch(branchName);
        history.add(s1);
        if (!s1.success()) return new DeploymentReport(false, history, server.getCurrentVersion());

        StageResult s2 = compiler.compile(branchName);
        history.add(s2);
        if (!s2.success()) return new DeploymentReport(false, history, server.getCurrentVersion());

        StageResult s3 = tests.runTests(branchName);
        history.add(s3);
        if (!s3.success()) return new DeploymentReport(false, history, server.getCurrentVersion());

        StageResult s4 = packager.packageJar(branchName);
        history.add(s4);
        if (!s4.success()) return new DeploymentReport(false, history, server.getCurrentVersion());

        StageResult s5 = server.deploy("build-" + branchName);
        history.add(s5);
        return new DeploymentReport(s5.success(), history, server.getCurrentVersion());
    }
}

public class Main {
    public static void main(String[] args) {
        DeploymentFacade cicd = new DeploymentFacade(
                new GitRepoFetcher(), new CompilerService(), new UnitTestRunner(),
                new ArtifactPackager(), new StagingServer()
        );

        
        System.out.println(cicd.deployBuild("feature/failing-test"));

        
        System.out.println(cicd.deployBuild("release/v1.5.0"));
    }
}