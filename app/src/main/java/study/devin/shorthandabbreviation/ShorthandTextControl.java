package study.devin.shorthandabbreviation;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ShorthandTextControl {

    private final String ENCODING_UCS_2 = "UCS-2";
    private String fullPath;
    private ArrayList<AbbreviationItemByMap> listAbbreviationItem;
    private HashMap<AbbreviationItemByMap, MapValue> mapAbbreviationItem;

    private String text;
    private FileInputStream fis;
    private boolean isStream;

    public ShorthandTextControl(FileInputStream fis) {
        this.fis = fis;
        isStream = true;
    }

    public ShorthandTextControl(String fullPath) {
        this.fullPath = fullPath;
        isStream = false;
    }

    public ArrayList<AbbreviationItemByMap> readTextFileToList() {
        listAbbreviationItem = new ArrayList<>();
        readTextFile(true);
        return listAbbreviationItem;
    }

    // Map은 사용하지 않음
    public HashMap<AbbreviationItemByMap, MapValue> readTextFileToMap() {
        mapAbbreviationItem = new HashMap<>();
        readTextFile(false);
        return mapAbbreviationItem;
    }

    private void readTextFile(boolean exportList) {
        try {
            BufferedReader bufferReader;
            if(isStream) {
                bufferReader = new BufferedReader(new InputStreamReader(this.fis));
            }
            else {
                FileInputStream fis = new FileInputStream(fullPath);
                bufferReader = new BufferedReader(new InputStreamReader(fis, ENCODING_UCS_2));
                // 약어 단어 사전은 UCS-2 리틀 엔디언으로 인코딩되어 있음
            }

            text = "";

            int count = 0;
            String temp = "";
            while ((temp = bufferReader.readLine()) != null) {
                text += temp + "\n";

                // 주석 걸러네기 >> ;
                if (temp.indexOf(';') != 0) {
                    createAbbreviationItem(count, temp, exportList);
                    count += 1;
                }
            }
            bufferReader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAbbreviationItem(int lineNum, String line, boolean exportList) {
        /*
            1. ';'로 시작하면 주석
            2. 초성중성종성숫자 내용으로 구성 / 약어 키와 내용 사이에는 탭(TAB)으로 구분
            2.1. 각 초성, 중성, 종성, 숫자는 키보드의 배열에 맞게 정렬되어 있다.
            2.2. 따라서 추가로 정렬할 필요가 없다.
            3. 중성 - ㅢ, ㅗ,ㅏ,ㅜ,ㅡ,ㅓ,ㅣ
            4. 중성이 없고 종성이 나올 경우 '-'로 구분
            4.1. ㄴ :: 초성 ㄴ
            4.1. -ㄴ :: 종성 ㄴ
        * */
        String content = line.substring(line.lastIndexOf('\t')+1);
        line = line.substring(0, line.indexOf('\t'));

        String initialC = "";
        String middleC  = "";
        String finalC   = "";
        String num      = "";

        char[] charArray = line.toCharArray();
        boolean isMiddle = false;
        for(char ch : charArray) {
            // 중성 확인
            if((ch == '-') || (ch == 'ㅢ') || (ch == 'ㅗ') || (ch == 'ㅏ') || (ch == 'ㅜ') || (ch == 'ㅡ') || (ch == 'ㅓ') || (ch == 'ㅣ')) {
                isMiddle = true;
                if(ch != '-')
                    middleC += ch;
            }
            // 중성 flag가 활성화되어 있지 않고, 중성이 아닌 경우 초성 또는 숫자이다.
            else if(!isMiddle) {
                if(ch >= '0' && ch <= '9')
                    num += ch;
                else
                    initialC += ch;
            }
            // 중성 flag가 활성화되어 있고, 중성이 아닌 경우 종성 또는 숫자이다.
            else { //if(isMiddle) {
                if(ch >= '0' && ch <= '9' )
                    num += ch;
                else
                    finalC += ch;
            }
        }

        if(exportList) {
            AbbreviationItemByMap newItem = new AbbreviationItemByMap.Builder()
                    .setInitialConsonant(initialC).setMiddleConsonant(middleC)
                    .setFinalConsonant(finalC).setNumber(num).setContent(content).build();
            listAbbreviationItem.add(newItem);
        }
        else {
            AbbreviationItemByMap newItem = new AbbreviationItemByMap.Builder()
                    .setInitialConsonant(initialC).setMiddleConsonant(middleC)
                    .setFinalConsonant(finalC).setNumber(num).build();
            mapAbbreviationItem.put(newItem, new MapValue(lineNum, content));
        }
    }

/*    // 각 String의 Char를 정렬하고, 해당 기능을 설정 Activity 옵션으로 활성화 여부를 결정하도록 하자.
    private String setSortingChar(char[] chArray) {
        if(chArray.length < 1)
            return null;

        for(char ch : chArray) {
        }
        return "";
    }*/

    public void setInitFullText() {text = "";}
    public String getFullText() {return text;}
}
