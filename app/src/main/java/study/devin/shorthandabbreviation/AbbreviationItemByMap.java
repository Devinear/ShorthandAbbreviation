package study.devin.shorthandabbreviation;

/**
 * 약어 하나의 정보를 담고 있다.
 * 빌더 패턴을 사용해보자.
 */

public class AbbreviationItemByMap {

    private String initial_consonant;   // 초성
    private String middle_consonant;    // 중성
    private String final_consonant;     // 종성
    private String number;
    private String content;

    public static class Builder {
        // 전부 선택 매개변수들이다.
        private String initial_consonant;
        private String middle_consonant;
        private String final_consonant;
        private String number;
        private String content;

        Builder() {
        }

        public Builder setInitialConsonant(String initialConsonant) {
            if(initialConsonant.compareTo("") != 0)
                this.initial_consonant = initialConsonant;
            return this;
        }

        public Builder setMiddleConsonant(String middleConsonant) {
            if(middleConsonant.compareTo("") != 0)
                this.middle_consonant = middleConsonant;
            return this;
        }

        public Builder setFinalConsonant(String finalConsonant) {
            if(finalConsonant.compareTo("") != 0)
                this.final_consonant = finalConsonant;
            return this;
        }

        public Builder setNumber(String number) {
            if(number.compareTo("") != 0)
                this.number = number;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public AbbreviationItemByMap build() {
            return new AbbreviationItemByMap(this);
        }
    }

    private AbbreviationItemByMap(Builder builder) {
        this.initial_consonant = builder.initial_consonant;
        this.middle_consonant = builder.middle_consonant;
        this.final_consonant = builder.final_consonant;
        this.number = builder.number;
        this.content = builder.content;
    }

    public String getInitialConsonant() {
        return this.initial_consonant;
    }
    public String getMiddleConsonant() {
        return this.middle_consonant;
    }
    public String getFinalConsonant() {
        return this.final_consonant;
    }
    public String getNumber() {
        return this.number;
    }
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean compare(AbbreviationItemByMap compareItem) {

        // initial
        if(this.initial_consonant != null && compareItem.getInitialConsonant() != null
                && this.initial_consonant.compareTo(compareItem.getInitialConsonant()) == 0) {
        }
        else if(this.initial_consonant == null && compareItem.getInitialConsonant() == null) {
        }
        else {
            return false;
        }

        // Middle
        if(this.middle_consonant != null && compareItem.getMiddleConsonant() != null
                && this.middle_consonant.compareTo(compareItem.getMiddleConsonant()) == 0) {
        }
        else if(this.middle_consonant == null && compareItem.getMiddleConsonant() == null) {
        }
        else {
            return false;
        }

        // Final
        if(this.final_consonant != null && compareItem.getFinalConsonant() != null
                && this.final_consonant.compareTo(compareItem.getFinalConsonant()) == 0) {
        }
        else if(this.final_consonant == null && compareItem.getFinalConsonant() == null) {
        }
        else {
            return false;
        }

        // Number
        if(this.number != null && compareItem.getNumber() != null
                && this.number.compareTo(compareItem.getNumber()) == 0) {
        }
        else if(this.number == null && compareItem.getNumber() == null) {
        }
        else {
            return false;
        }

        return true;
    }

    public boolean containContent(String content) {
        if(content == null)
            return false;

        if(this.content != null && this.content.contains(content))
            return true;
        else
            return false;
    }
}
