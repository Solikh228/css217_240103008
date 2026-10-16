interface CharacterDefense {
    int getArmorRating();
}

class BasicTunic implements CharacterDefense {
    public int getArmorRating() { return 10; }
}


class IronShieldDecorator implements CharacterDefense {
    CharacterDefense cd;
    public IronShieldDecorator(CharacterDefense cd) { this.cd = cd; }
    public int getArmorRating() { return cd.getArmorRating() + 25; }
}

class HelmetDecorator implements CharacterDefense {
    CharacterDefense cd;
    public HelmetDecorator(CharacterDefense cd) { this.cd = cd; }
    public int getArmorRating() { return cd.getArmorRating() + 15; }
}

class MagicAuraDecorator implements CharacterDefense {
    CharacterDefense cd;
    public MagicAuraDecorator(CharacterDefense cd) { this.cd = cd; }
    public int getArmorRating() { 
        return cd.getArmorRating() * 2;
    }
}