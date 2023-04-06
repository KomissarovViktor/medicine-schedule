package org.komissarov;

public enum ConsumeState {
    PLANNED,TAKEN,MISSED;

    public static ConsumeState fromInteger(int x) {
        switch(x) {
            case 0:
                return PLANNED;
            case 1:
                return TAKEN;
            case 3:
                return MISSED;
        }
        return null;
    }
}
