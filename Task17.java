import java.util.*;

record PlayerTicket(String playerId, int mmr, String region, int waitSeconds) {}

interface MatchmakingStrategy {
    boolean canMatch(PlayerTicket anchor, PlayerTicket candidate, int pingMs);
}

class StrictSkillMmrStrategy implements MatchmakingStrategy {
    public boolean canMatch(PlayerTicket a, PlayerTicket c, int ping) {
        return Math.abs(a.mmr() - c.mmr()) <= 100 && ping <= 60;
    }
}

class LowLatencyPriorityStrategy implements MatchmakingStrategy {
    public boolean canMatch(PlayerTicket a, PlayerTicket c, int ping) {
        return ping <= 35 && Math.abs(a.mmr() - c.mmr()) <= 400;
    }
}

class CasualQuickFillStrategy implements MatchmakingStrategy {
    public boolean canMatch(PlayerTicket a, PlayerTicket c, int ping) { return ping <= 150; }
}

class PlayerQueueManager {
    List<PlayerTicket> getCandidates() {
        return List.of(new PlayerTicket("p2", 1820, "EU", 10), new PlayerTicket("p3", 1450, "EU", 50));
    }
}

class RegionPingEvaluator {
    int estimatePing(String r1, String r2) { return r1.equals(r2) ? 25 : 115; }
}

class PartyBalanceValidator {
    boolean validateLobby(List<PlayerTicket> players) { return players.size() >= 2; }
}

class GameServerProvisioner {
    String allocateServer(List<PlayerTicket> players) { return "SERVER_READY(" + players.size() + " players)"; }
}

class MatchmakingFacade {
    private final MatchmakingStrategy primary;
    private final int fallbackThresholdSec;
    private final PlayerQueueManager queue = new PlayerQueueManager();
    private final RegionPingEvaluator pingEval = new RegionPingEvaluator();
    private final PartyBalanceValidator validator = new PartyBalanceValidator();
    private final GameServerProvisioner provisioner = new GameServerProvisioner();

    MatchmakingFacade(MatchmakingStrategy primary, int fallbackThresholdSec) {
        this.primary = primary;
        this.fallbackThresholdSec = fallbackThresholdSec;
    }

    String findMatch(PlayerTicket anchor) {
        MatchmakingStrategy active = anchor.waitSeconds() > fallbackThresholdSec ? new CasualQuickFillStrategy() : primary;
        List<PlayerTicket> lobby = new ArrayList<>();
        lobby.add(anchor);
        for (PlayerTicket c : queue.getCandidates()) {
            int ping = pingEval.estimatePing(anchor.region(), c.region());
            if (active.canMatch(anchor, c, ping)) lobby.add(c);
        }
        return validator.validateLobby(lobby) ? provisioner.allocateServer(lobby) : "QUEUED";
    }
}