package study.devin.shorthandabbreviation;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileExplorerActivity extends ListActivity {

    // item = 탐색기에 표시될 내용
    // path = item 클릭시 이동할 경로
    private List<String> listItem = null;
    private List<String> listPath = null;
    //private final String ROOT_DIR = "/storage/emulated/0";
    private String rootDirectory;

    private TextView tvMyPath;

    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    public FileExplorerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);

        // TEXT VIEW
        tvMyPath = (TextView) findViewById(R.id.textView_myPath);

        // 외부 저장소 파일 읽기에 대한 권한 확인
        boolean isPermission = checkPermission();

        // 권한이 없을 경우 권한 허용 후에 시작한다.
        if(isPermission)
            getRootDirectory();
    }

    // 권한 확인하기
    private boolean checkPermission() {
        // 파일 읽기 권한 확인
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 권한 미보유
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
        }
        else {
            return true;
        }
    }

    // 루트 폴더의 위치 가져오기
    private void getRootDirectory() {
        // 외부 저장소 상태 확인
        if(checkExternalStorageState()) {
            // 읽기 가능
            File rootFile = Environment.getExternalStorageDirectory();
            rootDirectory = rootFile.getPath();
            getDirectory(rootDirectory);
        }
        else {
            // 읽기 불가능
            finish();
        }
    }

    // 외부 저장소 상태 확인
    private boolean checkExternalStorageState() {
        String state = Environment.getExternalStorageState();
        switch (state) {
            case Environment.MEDIA_MOUNTED:
                // 읽기 쓰기 모두 가능
                return true;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                // 읽기만 가능
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                getRootDirectory();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this, getString(R.string.file_permission_denied), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void getDirectory(String strDirPath) {
        String myPath = getString(R.string.file_explorer_path_title);
        myPath += strDirPath;
        tvMyPath.setText(myPath);
        listItem = new ArrayList<>();
        listPath = new ArrayList<>();

        // File 객체 생성 및 하위 디렉토리/파일 행렬 생성
        File file = new File(strDirPath);
        File[] files = file.listFiles();
        if(files == null) {
            Toast.makeText(this, "ROOT 위치 잘못됨", Toast.LENGTH_SHORT).show();
            finish();
        }
        // 정렬
        Arrays.sort(files, new FileSort());

        // 상위 및 루트 추가
        if(!strDirPath.equals(rootDirectory)) {
            // ROOT 설정
            listItem.add(rootDirectory);
            listPath.add(rootDirectory);

            // 상위 디렉토리 및 경로
            listItem.add("../");
            listPath.add(file.getParent());
        }

        // 하위 폴더 및 파일 추가
        int nFileCount = files.length;
        for(int i =0; i <nFileCount; i++) {
            File fileTemp = files[i];
            listPath.add(fileTemp.getPath());

            if(fileTemp.isDirectory())
                listItem.add(fileTemp.getName() + "/");
            else
                listItem.add(fileTemp.getName());
        }

        // ArrayAdapter를 통해 path에서의 파일 탐색기 뷰 생성
        ArrayAdapter<String> adapterFileList = new ArrayAdapter<String>(this, R.layout.item_row, listItem);
        setListAdapter(adapterFileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//      현재 뷰가 root 디렉토리가 아닌 경우
//      path.get(0) = root 경로
//      path.get(1) = 상위 디렉토리 경로
//      path.get(2) = getDir에서 저장된 하위 디렉토리/파일 경로

        File file = new File(listPath.get(position));

        // 클릭한 아이템이 폴더인 경우
        if(file.isDirectory()) {
            // 접근 가능
            if(file.canRead())
                getDirectory(file.getPath());
            // 접근 불가
            else
                Toast.makeText(this, "해당 폴더에 접근할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        // 클릭한 아이템이 파일인 경우
        else { // if(file.isFile()) {
            String fullName = file.getName();
            String ext = fullName.substring(fullName.indexOf('.'));

            // 확장자가 txt면 Path 전달 후 종료
            if(ext.compareTo(".txt") == 0) {
                Intent intent  = getIntent();
                intent.putExtra("fullPath", listPath.get(position));
                setResult(RESULT_OK,intent);
                finish();
            }
            else {
                Toast.makeText(this, "잘못된 파일을 클릭하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FileSort implements Comparator<File> {

        @Override
        public int compare(File o1, File o2) {
            // 1. 폴더가 우선순위가 높음
            if(o1.isDirectory() && o2.isFile())
                return -1;
            else if(o1.isFile() && o2.isDirectory())
                return 1;
            // 2. 같을 경우 이름순으로 정렬
            else
                // 비교는 대소문자 구분하지 않음
                return compareString(o1.getName().toLowerCase(), o2.getName().toLowerCase());
        }

        private int compareString(String strA, String strB) {
            int lengthA = strA.length();
            int lengthB = strB.length();
            int minLength = Math.min(lengthA, lengthB);
            for(int i =0; i < minLength; i++) {
                int compare = compareChar(strA.charAt(i), strB.charAt(i));
                if(compare != 0)
                    return compare;
            }
            // 같은 범위내에서 동일한 경우에 길이가 짧은 것에 우선순위를 준다.
            if(lengthA > lengthB)
                return -1;
            else if(lengthA == lengthB)
                return 0;
            else
                return 1;
        }

        private int compareChar(char chA, char chB) {
            if(chA < chB)
                return -1;
            else if(chA == chB)
                return 0;
            else
                return 1;
        }
    }
}
