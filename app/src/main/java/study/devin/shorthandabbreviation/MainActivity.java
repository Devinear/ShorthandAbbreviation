package study.devin.shorthandabbreviation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
    * To Do List
    * 1. 설정 기능 Activity 추가
    *
    * 2. File Manager 고도화
    * 2.1. 즐겨찾기, Root 폴더, 특정 파일(txt)만 보기, 숨김 파일 및 폴더 보여주지 않기
    * 2.2. File Manager를 Activity가 아닌 플롯팅 화면으로 구성하기
    *
    * 3. Main Activity의 Word List 고도화
    * 3.1. 페이징 처리 - 500, 1000, 1500 개 등의 개수 나눔. 개수는 설정 Activity에서 사용자가 설정할 수 있도록 하자.
    * 3.2. 플리킹 중에는 리스트 보여주지 않기 - User가 거부함
    * 3.3. 플로팅 액션 버튼 추가
    * */

    // DEFINE
    private final int FILE_EXPLORER_ACTIVITY = 1;
    private final String INTERNAL_READ = "InternalRead";
    private final String EXTERNAL_READ = "ExternalRead";
    private final String FILE_INTERNAL_NAME = "AbbreviationList";

    // Value
    private ListView listViewAbbre;
    private AbbreviationAdapter adapterAbbre;

    // Value - Toggle Btn & EditText
    private ToggleButton[] arrToggleBtnInitialC;
    private ToggleButton[] arrToggleBtnMiddleC;
    private ToggleButton[] arrToggleBtnFinalC;
    private ToggleButton[] arrToggleBtnNumberC;
    private EditText editSearchText;

    private int searchPosition;
    private boolean isListMovable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FileExplorerActivity.class);
                startActivityForResult(intent, FILE_EXPLORER_ACTIVITY);
            }
        });

        // UI 기본 설정
        initToggleBtn();
        initCommonUI();

        // 내부 문서 읽기
        readInternalStorage();
    }

    // Toggle Btn 초기화
    private void initToggleBtn() {

        // Initial Consonant Toggle Btn - 초성
        arrToggleBtnInitialC = new ToggleButton[13];
        arrToggleBtnInitialC[0] = (ToggleButton) findViewById(R.id.toggleBtn_initial_1);
        arrToggleBtnInitialC[1] = (ToggleButton) findViewById(R.id.toggleBtn_initial_2);
        arrToggleBtnInitialC[2] = (ToggleButton) findViewById(R.id.toggleBtn_initial_3);
        arrToggleBtnInitialC[3] = (ToggleButton) findViewById(R.id.toggleBtn_initial_4);
        arrToggleBtnInitialC[4] = (ToggleButton) findViewById(R.id.toggleBtn_initial_5);
        arrToggleBtnInitialC[5] = (ToggleButton) findViewById(R.id.toggleBtn_initial_6);
        arrToggleBtnInitialC[6] = (ToggleButton) findViewById(R.id.toggleBtn_initial_7);
        arrToggleBtnInitialC[7] = (ToggleButton) findViewById(R.id.toggleBtn_initial_8);
        arrToggleBtnInitialC[8] = (ToggleButton) findViewById(R.id.toggleBtn_initial_9);
        arrToggleBtnInitialC[9] = (ToggleButton) findViewById(R.id.toggleBtn_initial_10);
        arrToggleBtnInitialC[10] = (ToggleButton) findViewById(R.id.toggleBtn_initial_11);
        arrToggleBtnInitialC[11] = (ToggleButton) findViewById(R.id.toggleBtn_initial_12);
        arrToggleBtnInitialC[12] = (ToggleButton) findViewById(R.id.toggleBtn_initial_13);

        // Middle Consonant Toggle Btn - 중성
        arrToggleBtnMiddleC = new ToggleButton[7];
        arrToggleBtnMiddleC[0] = (ToggleButton) findViewById(R.id.toggleBtn_middle_1);  // 'ㅢ' - 초성에 위치함
        arrToggleBtnMiddleC[1] = (ToggleButton) findViewById(R.id.toggleBtn_middle_2);
        arrToggleBtnMiddleC[2] = (ToggleButton) findViewById(R.id.toggleBtn_middle_3);
        arrToggleBtnMiddleC[3] = (ToggleButton) findViewById(R.id.toggleBtn_middle_4);
        arrToggleBtnMiddleC[4] = (ToggleButton) findViewById(R.id.toggleBtn_middle_5);
        arrToggleBtnMiddleC[5] = (ToggleButton) findViewById(R.id.toggleBtn_middle_6);
        arrToggleBtnMiddleC[6] = (ToggleButton) findViewById(R.id.toggleBtn_middle_7);

        // Final Consonant Toggle Btn - 종성
        arrToggleBtnFinalC = new ToggleButton[16];;
        arrToggleBtnFinalC[0] = (ToggleButton) findViewById(R.id.toggleBtn_final_1);
        arrToggleBtnFinalC[1] = (ToggleButton) findViewById(R.id.toggleBtn_final_2);
        arrToggleBtnFinalC[2] = (ToggleButton) findViewById(R.id.toggleBtn_final_3);
        arrToggleBtnFinalC[3] = (ToggleButton) findViewById(R.id.toggleBtn_final_4);
        arrToggleBtnFinalC[4] = (ToggleButton) findViewById(R.id.toggleBtn_final_5);
        arrToggleBtnFinalC[5] = (ToggleButton) findViewById(R.id.toggleBtn_final_6);
        arrToggleBtnFinalC[6] = (ToggleButton) findViewById(R.id.toggleBtn_final_7);
        arrToggleBtnFinalC[7] = (ToggleButton) findViewById(R.id.toggleBtn_final_8);
        arrToggleBtnFinalC[8] = (ToggleButton) findViewById(R.id.toggleBtn_final_9);
        arrToggleBtnFinalC[9] = (ToggleButton) findViewById(R.id.toggleBtn_final_10);
        arrToggleBtnFinalC[10] = (ToggleButton) findViewById(R.id.toggleBtn_final_11);
        arrToggleBtnFinalC[11] = (ToggleButton) findViewById(R.id.toggleBtn_final_12);
        arrToggleBtnFinalC[12] = (ToggleButton) findViewById(R.id.toggleBtn_final_13);
        arrToggleBtnFinalC[13] = (ToggleButton) findViewById(R.id.toggleBtn_final_14);
        arrToggleBtnFinalC[14] = (ToggleButton) findViewById(R.id.toggleBtn_final_15);
        arrToggleBtnFinalC[15] = (ToggleButton) findViewById(R.id.toggleBtn_final_16);  // 'ㅋ' - 초성에 위치함

        // Number Toggle Btn
        arrToggleBtnNumberC = new ToggleButton[10];
        arrToggleBtnNumberC[0] = (ToggleButton) findViewById(R.id.toggleBtn_num_1);
        arrToggleBtnNumberC[1] = (ToggleButton) findViewById(R.id.toggleBtn_num_2);
        arrToggleBtnNumberC[2] = (ToggleButton) findViewById(R.id.toggleBtn_num_3);
        arrToggleBtnNumberC[3] = (ToggleButton) findViewById(R.id.toggleBtn_num_4);
        arrToggleBtnNumberC[4] = (ToggleButton) findViewById(R.id.toggleBtn_num_5);
        arrToggleBtnNumberC[5] = (ToggleButton) findViewById(R.id.toggleBtn_num_6);
        arrToggleBtnNumberC[6] = (ToggleButton) findViewById(R.id.toggleBtn_num_7);
        arrToggleBtnNumberC[7] = (ToggleButton) findViewById(R.id.toggleBtn_num_8);
        arrToggleBtnNumberC[8] = (ToggleButton) findViewById(R.id.toggleBtn_num_9);
        arrToggleBtnNumberC[9] = (ToggleButton) findViewById(R.id.toggleBtn_num_0);

        for(ToggleButton btn : arrToggleBtnInitialC)
            btn.setOnCheckedChangeListener(onToggleListener);
        for(ToggleButton btn : arrToggleBtnMiddleC)
            btn.setOnCheckedChangeListener(onToggleListener);
        for(ToggleButton btn : arrToggleBtnFinalC)
            btn.setOnCheckedChangeListener(onToggleListener);
        for(ToggleButton btn : arrToggleBtnNumberC)
            btn.setOnCheckedChangeListener(onToggleListener);
    }

    // 기타 UI 초기화
    private void initCommonUI() {

        // Line Break - 줄 바꿈
        Button btnLineBreak = (Button) findViewById(R.id.btn_linebreak);
        btnLineBreak.setOnClickListener(onClickListener);
        // 실제 키보드의 화면을 구성하기 위하여 추가하였지만, 약어 단어를 보기위한 용도에서는 불필요하므로 비활성화함
        btnLineBreak.setEnabled(false);

        // 버튼 체크 초기화 초기화
        Button btnUnchecked = (Button) findViewById(R.id.btn_clear);
        btnUnchecked.setOnClickListener(onClickListener);

        // 검색 및 내용 지우기
        ImageButton btnSearch = (ImageButton) findViewById(R.id.btn_search_next);
        btnSearch.setOnClickListener(onClickListener);
        ImageButton btnSearchDelete = (ImageButton) findViewById(R.id.btn_search_clear);
        btnSearchDelete.setOnClickListener(onClickListener);

        // 검색 EDIT TEXT
        editSearchText = (EditText) findViewById(R.id.edit_search);
        editSearchText.setText("");

        // 검색이 시작되는 위치 - 다음 검색시에 사용
        searchPosition = 0;

        // 리스트뷰에서 아이템을 선택시에는 토글 버튼의 리스너 동작을 막기 위함
        isListMovable = true;

        // List View 설정
        adapterAbbre = new AbbreviationAdapter(this);
        listViewAbbre = (ListView) findViewById(R.id.abbreviation_listview);
        listViewAbbre.setAdapter(adapterAbbre);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private CompoundButton.OnCheckedChangeListener onToggleListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            // ListView의 아이템을 클릭하여 토글 버튼을 체크할 경우에는 하기 동작의 의미가 없다.
            if(!isListMovable)
                return;

            String initialC = "";
            for(ToggleButton btn : arrToggleBtnInitialC) {
                if(btn.isChecked())
                    initialC += getToggleBtnText(btn.getId());
            }
            String middleC = "";
            for(ToggleButton btn : arrToggleBtnMiddleC) {
                if(btn.isChecked())
                    middleC += getToggleBtnText(btn.getId());
            }
            String finalC = "";
            for(ToggleButton btn : arrToggleBtnFinalC) {
                if(btn.isChecked())
                    finalC += getToggleBtnText(btn.getId());
            }
            String numberC = "";
            for(ToggleButton btn : arrToggleBtnNumberC) {
                if(btn.isChecked())
                    numberC += getToggleBtnText(btn.getId());
            }

            AbbreviationItemByMap selItemByMap = new AbbreviationItemByMap.Builder()
                    .setInitialConsonant(initialC).setMiddleConsonant(middleC)
                    .setFinalConsonant(finalC).setNumber(numberC).build();

            // 이동하며 해당 아이템에 하이라이트 설정
            int position = adapterAbbre.getPosition(selItemByMap);
            if(position >= 0)
                setListItemHighlight(true, position, true);
            else
                setListItemHighlight(false, -1, false);
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_clear:
                    // 체크되어 있는 버튼을 해제
                    setUncheckedToggleBtn();
                    break;
                case R.id.btn_linebreak:
                    // 비활성화 처리
                    break;
                case R.id.btn_search_next:
                    // 검색을 진행하면 버튼의 체크 상태를 우선 해제한다.
                    setUncheckedToggleBtn();

                    // 검색 TEXT 가져오기
                    String searchText = editSearchText.getText().toString();
                    int position = adapterAbbre.getPositionContent(searchPosition, searchText);

                    if(position >= 0) {
                        searchPosition = position +1;
                        if(searchPosition == adapterAbbre.getCount())
                            searchPosition = 0;

                        // 하이라이트 설정
                        setListItemHighlight(true, position, true);

                        // 검색된 아이템의 토글버튼이 체크되도록
                        AbbreviationItemByMap item = adapterAbbre.getItem(position);
                        if(item != null)
                            itemToToggleBtn(item);
                    }
                    else {
                        // 검색 결과가 없으면 하이라이트 해제
                        setListItemHighlight(false, -1, false);
                        searchPosition = 0;

                        Toast.makeText(getApplicationContext(), getString(R.string.no_results_found), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_search_clear:
                    // 검색된 결과가 있을 경우 하이라이트 및 버튼의 체크 상태는 유지하도록 하자
                    //setListItemHighlight(false, -1, false);
                    //setUncheckedToggleBtn();
                    editSearchText.setText("");
                    break;
            }
        }
    };

    // 토글 버튼 체크 해제
    private void setUncheckedToggleBtn() {
        for(ToggleButton btn : arrToggleBtnInitialC) {
            if(btn.isChecked()) btn.setChecked(false);
        }
        for(ToggleButton btn : arrToggleBtnMiddleC) {
            if(btn.isChecked()) btn.setChecked(false);
        }
        for(ToggleButton btn : arrToggleBtnFinalC) {
            if(btn.isChecked()) btn.setChecked(false);
        }
        for(ToggleButton btn : arrToggleBtnNumberC) {
            if(btn.isChecked()) btn.setChecked(false);
        }
    }

    // ListView의 하이라이트 설정
    private void setListItemHighlight(boolean isHighlight, int position, boolean isSelection) {
        adapterAbbre.setItemHighlight(isHighlight, position);
        adapterAbbre.notifyDataSetChanged();

        // 하이라이트 설정시 해당 아이템으로 이동
        if(isHighlight && isSelection)
            listViewAbbre.setSelection(position);
    }

    // 토글 버튼의 텍스트 가져오기
    private String getToggleBtnText(int id) {
        String text = "";
        switch (id) {
            // 초성
            case R.id.toggleBtn_initial_1:  text += getString(R.string.initial_consonant_1);  break;
            case R.id.toggleBtn_initial_2:  text += getString(R.string.initial_consonant_2);  break;
            case R.id.toggleBtn_initial_3:  text += getString(R.string.initial_consonant_3);  break;
            case R.id.toggleBtn_initial_4:  text += getString(R.string.initial_consonant_4);  break;
            case R.id.toggleBtn_initial_5:  text += getString(R.string.initial_consonant_5);  break;
            case R.id.toggleBtn_initial_6:  text += getString(R.string.initial_consonant_6);  break;
            case R.id.toggleBtn_initial_7:  text += getString(R.string.initial_consonant_7);  break;
            case R.id.toggleBtn_initial_8:  text += getString(R.string.initial_consonant_8);  break;
            case R.id.toggleBtn_initial_9:  text += getString(R.string.initial_consonant_9);  break;
            case R.id.toggleBtn_initial_10: text += getString(R.string.initial_consonant_10);  break;
            case R.id.toggleBtn_initial_11: text += getString(R.string.initial_consonant_11);  break;
            case R.id.toggleBtn_initial_12: text += getString(R.string.initial_consonant_12);  break;
            case R.id.toggleBtn_initial_13: text += getString(R.string.initial_consonant_13);  break;
            // 중성
            case R.id.toggleBtn_middle_1:   text += getString(R.string.middle_consonant_1);  break;
            case R.id.toggleBtn_middle_2:   text += getString(R.string.middle_consonant_2);  break;
            case R.id.toggleBtn_middle_3:   text += getString(R.string.middle_consonant_3);  break;
            case R.id.toggleBtn_middle_4:   text += getString(R.string.middle_consonant_4);  break;
            case R.id.toggleBtn_middle_5:   text += getString(R.string.middle_consonant_5);  break;
            case R.id.toggleBtn_middle_6:   text += getString(R.string.middle_consonant_6);  break;
            case R.id.toggleBtn_middle_7:   text += getString(R.string.middle_consonant_7);  break;
            // 종성
            case R.id.toggleBtn_final_1:    text += getString(R.string.final_consonant_1);  break;
            case R.id.toggleBtn_final_2:    text += getString(R.string.final_consonant_2);  break;
            case R.id.toggleBtn_final_3:    text += getString(R.string.final_consonant_3);  break;
            case R.id.toggleBtn_final_4:    text += getString(R.string.final_consonant_4);  break;
            case R.id.toggleBtn_final_5:    text += getString(R.string.final_consonant_5);  break;
            case R.id.toggleBtn_final_6:    text += getString(R.string.final_consonant_6);  break;
            case R.id.toggleBtn_final_7:    text += getString(R.string.final_consonant_7);  break;
            case R.id.toggleBtn_final_8:    text += getString(R.string.final_consonant_8);  break;
            case R.id.toggleBtn_final_9:    text += getString(R.string.final_consonant_9);  break;
            case R.id.toggleBtn_final_10:   text += getString(R.string.final_consonant_10);  break;
            case R.id.toggleBtn_final_11:   text += getString(R.string.final_consonant_11);  break;
            case R.id.toggleBtn_final_12:   text += getString(R.string.final_consonant_12);  break;
            case R.id.toggleBtn_final_13:   text += getString(R.string.final_consonant_13);  break;
            case R.id.toggleBtn_final_14:   text += getString(R.string.final_consonant_14);  break;
            case R.id.toggleBtn_final_15:   text += getString(R.string.final_consonant_15);  break;
            case R.id.toggleBtn_final_16:   text += getString(R.string.final_consonant_16);  break;
            // 숫자
            case R.id.toggleBtn_num_0:      text += getString(R.string.number_0);  break;
            case R.id.toggleBtn_num_1:      text += getString(R.string.number_1);  break;
            case R.id.toggleBtn_num_2:      text += getString(R.string.number_2);  break;
            case R.id.toggleBtn_num_3:      text += getString(R.string.number_3);  break;
            case R.id.toggleBtn_num_4:      text += getString(R.string.number_4);  break;
            case R.id.toggleBtn_num_5:      text += getString(R.string.number_5);  break;
            case R.id.toggleBtn_num_6:      text += getString(R.string.number_6);  break;
            case R.id.toggleBtn_num_7:      text += getString(R.string.number_7);  break;
            case R.id.toggleBtn_num_8:      text += getString(R.string.number_8);  break;
            case R.id.toggleBtn_num_9:      text += getString(R.string.number_9);  break;
        }
        return text;
    }

    // 텍스트에 맞는 토글 버튼 체크하기
    private void setToggleBtnFromText_Initial(char ch) {
        switch (ch) {
            case 'ㅊ' :  arrToggleBtnInitialC[0].setChecked(true); break;
            case 'ㅌ' :  arrToggleBtnInitialC[1].setChecked(true); break;
            case 'ㅋ' :  arrToggleBtnInitialC[2].setChecked(true); break;
            case 'ㅂ' :  arrToggleBtnInitialC[3].setChecked(true); break;
            case 'ㅍ' :  arrToggleBtnInitialC[4].setChecked(true); break;
            case 'ㅅ' :  arrToggleBtnInitialC[5].setChecked(true); break;
            case 'ㄷ' :  arrToggleBtnInitialC[6].setChecked(true); break;
            case 'ㅈ' :  arrToggleBtnInitialC[7].setChecked(true); break;
            case 'ㄱ' :  arrToggleBtnInitialC[8].setChecked(true); break;
            case 'ㅁ' :  arrToggleBtnInitialC[9].setChecked(true); break;
            case 'ㄹ' :  arrToggleBtnInitialC[10].setChecked(true); break;
            case 'ㄴ' :  arrToggleBtnInitialC[11].setChecked(true); break;
            case 'ㅎ' :  arrToggleBtnInitialC[12].setChecked(true); break;
            default:    break;
        }
    }
    private void setToggleBtnFromText_Middle(char ch) {
        switch (ch) {
            case 'ㅢ' :  arrToggleBtnMiddleC[0].setChecked(true); break;
            case 'ㅗ' :  arrToggleBtnMiddleC[1].setChecked(true); break;
            case 'ㅏ' :  arrToggleBtnMiddleC[2].setChecked(true); break;
            case 'ㅜ' :  arrToggleBtnMiddleC[3].setChecked(true); break;
            case 'ㅡ' :  arrToggleBtnMiddleC[4].setChecked(true); break;
            case 'ㅓ' :  arrToggleBtnMiddleC[5].setChecked(true); break;
            case 'ㅣ' :  arrToggleBtnMiddleC[6].setChecked(true); break;
            default:    break;
        }
    }
    private void setToggleBtnFromText_Final(char ch) {
        switch (ch) {
            case 'ㄲ' :  arrToggleBtnFinalC[0].setChecked(true); break;
            case 'ㅎ' :  arrToggleBtnFinalC[1].setChecked(true); break;
            case 'ㅌ' :  arrToggleBtnFinalC[2].setChecked(true); break;
            case 'ㅊ' :  arrToggleBtnFinalC[3].setChecked(true); break;
            case 'ㅍ' :  arrToggleBtnFinalC[4].setChecked(true); break;
            case 'ㅋ' :  arrToggleBtnFinalC[5].setChecked(true); break;
            case 'ㄱ' :  arrToggleBtnFinalC[6].setChecked(true); break;
            case 'ㄴ' :  arrToggleBtnFinalC[7].setChecked(true); break;
            case 'ㄹ' :  arrToggleBtnFinalC[8].setChecked(true); break;
            case 'ㅅ' :  arrToggleBtnFinalC[9].setChecked(true); break;
            case 'ㅂ' :  arrToggleBtnFinalC[10].setChecked(true); break;
            case 'ㅆ' :  arrToggleBtnFinalC[11].setChecked(true); break;
            case 'ㅇ' :  arrToggleBtnFinalC[12].setChecked(true); break;
            case 'ㅁ' :  arrToggleBtnFinalC[13].setChecked(true); break;
            case 'ㄷ' :  arrToggleBtnFinalC[14].setChecked(true); break;
            case 'ㅈ' :  arrToggleBtnFinalC[15].setChecked(true); break;
            default:    break;
        }
    }
    private void setToggleBtnFromText_Number(char ch) {
        switch (ch) {
            case '1' :  arrToggleBtnNumberC[0].setChecked(true); break;
            case '2' :  arrToggleBtnNumberC[1].setChecked(true); break;
            case '3' :  arrToggleBtnNumberC[2].setChecked(true); break;
            case '4' :  arrToggleBtnNumberC[3].setChecked(true); break;
            case '5' :  arrToggleBtnNumberC[4].setChecked(true); break;
            case '6' :  arrToggleBtnNumberC[5].setChecked(true); break;
            case '7' :  arrToggleBtnNumberC[6].setChecked(true); break;
            case '8' :  arrToggleBtnNumberC[7].setChecked(true); break;
            case '9' :  arrToggleBtnNumberC[8].setChecked(true); break;
            case '0' :  arrToggleBtnNumberC[9].setChecked(true); break;
            default:    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // File Explorer Activity 선택한 파일 전달
        if(requestCode == FILE_EXPLORER_ACTIVITY && resultCode == RESULT_OK) {
            String fullPath = data.getStringExtra("fullPath");
            Toast.makeText(this, fullPath, Toast.LENGTH_SHORT).show();

            // 선택한 파일 분석 시작
            try {
                new TextFileControlTask().execute(EXTERNAL_READ, fullPath);
            }
            catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 내부 문서 읽고, 분석 시작
    private void readInternalStorage() {
        try {
            new TextFileControlTask().execute(INTERNAL_READ, FILE_INTERNAL_NAME);
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // ListView에서 아이템을 선택시에 역으로 토글 버튼을 체크함
    public void clickedListItem(AbbreviationItemByMap clickedItem, int clickedPosition) {
        // 버튼 체크 해제
        setUncheckedToggleBtn();

        // 선택한 아이템 하이라이트 - 기존 하이라이트는 해제됨
        setListItemHighlight(true, clickedPosition, false);

        itemToToggleBtn(clickedItem);
    }

    // ITEM으로 Toggle Btn을 체크함
    private void itemToToggleBtn(AbbreviationItemByMap clickedItem) {
        // 토글 버튼의 리스너 동작을 막기 위함
        isListMovable = false;

        String initialC = clickedItem.getInitialConsonant();
        if(initialC != null) {
            for(char ch : initialC.toCharArray())
                setToggleBtnFromText_Initial(ch);
        }
        String middleC = clickedItem.getMiddleConsonant();
        if(middleC != null) {
            for(char ch : middleC.toCharArray())
                setToggleBtnFromText_Middle(ch);
        }
        String finalC = clickedItem.getFinalConsonant();
        if(finalC != null) {
            for(char ch : finalC.toCharArray())
                setToggleBtnFromText_Final(ch);
        }
        String numberC = clickedItem.getNumber();
        if(numberC != null) {
            for(char ch : numberC.toCharArray())
                setToggleBtnFromText_Number(ch);
        }

        isListMovable = true;
    }

    private class TextFileControlTask extends AsyncTask<String, Integer, ArrayList<AbbreviationItemByMap> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<AbbreviationItemByMap> doInBackground(String... params) {
            ArrayList<AbbreviationItemByMap> list = null;
            try {
                // Internal Storage Read
                if(params[0].compareTo(INTERNAL_READ) == 0) {
                    String internalPath = params[1];
                    FileInputStream fis = openFileInput(internalPath);
                    ShorthandTextControl textControl = new ShorthandTextControl(fis);
                    list = textControl.readTextFileToList();
                    fis.close();
                }
                // External Read
                else {
                    String externalPath = params[1];
                    ShorthandTextControl textControl = new ShorthandTextControl(externalPath);
                    list = textControl.readTextFileToList();

                    // Internal Storage Write
                    FileOutputStream file = openFileOutput(FILE_INTERNAL_NAME, MODE_PRIVATE);
                    file.write(textControl.getFullText().getBytes());
                    file.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<AbbreviationItemByMap> abbreviationItemByMaps) {
            super.onPostExecute(abbreviationItemByMaps);
            if(abbreviationItemByMaps != null && abbreviationItemByMaps.size() >0) {
                adapterAbbre.AddItemFromList(abbreviationItemByMaps);
                adapterAbbre.notifyDataSetChanged();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
