interface AttackStrategy {
    int computeDamage(int baseStrength);
}

class MeleeAttack implements AttackStrategy {
    public int computeDamage(int baseStrength) {
        return baseStrength * 2;
    }
}

class RangedAttack implements AttackStrategy {
    public int computeDamage(int baseStrength) {
        return baseStrength + 15;
    }
}

class MagicAttack implements AttackStrategy {
    public int computeDamage(int baseStrength) {
        return (baseStrength * 3) - 10; 
    }
}