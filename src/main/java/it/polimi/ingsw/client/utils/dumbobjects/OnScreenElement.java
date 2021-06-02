package it.polimi.ingsw.client.utils.dumbobjects;

public enum OnScreenElement {
    PERSONAL_BOARD1(1),
    PERSONAL_BOARD2(2),
    PERSONAL_BOARD3(3),
    PERSONAL_BOARD4(4),
    COMMONS(-1),
    FORCE_DISPLAY(-1),
    DONT_RENDER(-1);

    final int personalBoardNumber;

    OnScreenElement(int personalBoardNumber) {
        this.personalBoardNumber = personalBoardNumber;
    }

    public int getPersonalBoardNumber() {
        return personalBoardNumber;
    }

    public static OnScreenElement valueOf(int number) {
        for (OnScreenElement onScreenElement : values()) {
            if (onScreenElement.personalBoardNumber == number) {
                return onScreenElement;
            }
        }
        return null;
    }
}
