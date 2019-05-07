package tran.com.android.tapla.gamecenter.market.activity.module;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.transsion.ossdk.dialog.TalpaOssdkBottomDialog;
import com.transsion.ossdk.dialog.TalpaOssdkContentBottomDialog;
import com.transsion.ossdk.dialog.TalpaOssdkListBottomDialog;
import com.transsion.ossdk.dialog.TalpaOssdkListSingleCheckBottomDialog;
import com.transsion.ossdk.dialog.TalpaOssdkListSingleCheckWithIconBottomDialog;
import com.transsion.ossdk.dialog.TalpaOssdkSingleCheckBottomDialog;
import com.transsion.ossdk.dialog.TalpaOssdkWithEditBottomDialog;
import com.transsion.ossdk.dialog.data.TalpaOssdkDialogIconItemData;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogBtnContainer.DialogBtnClickListener;

import java.util.ArrayList;

import tran.com.android.tapla.gamecenter.R;

/**
 * Created by liang.wu1 on 2017/6/27.
 */

public class DialogDemoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_demo_activity);
    }

    public void showDialog(View view) {
        switch (view.getId()) {
            case R.id.show_bottom_list_dialog1:
                showBottomListDialog1();
                break;
            case R.id.show_bottom_list_dialog12:
                showBottomListDialog12();
                break;
            case R.id.show_bottom_list_dialog13:
                showBottomListDialog13();
                break;
            case R.id.show_bottom_list_dialog2:
                showBottomListDialog2();
                break;
            case R.id.show_edit_dialog:
                showEditDialg();
                break;
            case R.id.show_singleline_dialog:
                showSinglineDialog();
                break;
            case R.id.show_multiline_dialog:
                showMultiLineDialog();
                break;
            case R.id.show_multiline_with_title_dialog:
                showMultiLineWithTitleDialog();
                break;
            case R.id.show_left_icon_dialog:
                showLeftIconDialog();
                break;
            case R.id.show_single_check_dialog:
                showSingleCheckDialog();
                break;
        }
    }

    private void showBottomListDialog1() {
        TalpaOssdkListSingleCheckBottomDialog.showDialog(this, this.getResources().getTextArray(R.array.demo_btn_titles), getString(R.string.favorites_tab_label),
                1, new TalpaOssdkBottomDialog.OnDialogItemSelectListener<String>() {
                    @Override
                    public void onDialogItemSelect(int index, String chooseItemStr) {
                        android.util.Log.i("test", "Click index = " + index + ", chooseItemStr = " + chooseItemStr);
                    }
                });
    }

    private void showBottomListDialog12() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add("test " + i);
        }
        TalpaOssdkListSingleCheckBottomDialog.showDialog(this, list, getString(R.string.favorites_tab_label),
                "test 3", new TalpaOssdkBottomDialog.OnDialogItemSelectListener<String>() {
                    @Override
                    public void onDialogItemSelect(int index, String chooseItemStr) {
                        android.util.Log.i("test", "Click index = " + index + ", chooseItemStr = " + chooseItemStr);
                    }
                });
    }

    private void showBottomListDialog13() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            TalpaOssdkDialogIconItemData data;
            if (i == 3) {
                data = new TalpaOssdkDialogIconItemData(R.drawable.add_white, this.getString(R.string.delete),
                        null, true);
            } else {
                data = new TalpaOssdkDialogIconItemData(R.drawable.add_white, this.getString(R.string.delete),
                        this.getString(R.string.multiline_dialog_message), false);
            }
            list.add(data);
        }
        TalpaOssdkListSingleCheckWithIconBottomDialog.showIconListDialog(this, list, 3,
                getString(R.string.favorites_tab_label),
                new TalpaOssdkBottomDialog.OnDialogItemSelectListener<TalpaOssdkDialogIconItemData>() {
                    @Override
                    public void onDialogItemSelect(int index, TalpaOssdkDialogIconItemData chooseItemStr) {
                        android.util.Log.i("test", "Click index = " + index + ", chooseItemStr = " + chooseItemStr);
                    }
                });
    }

    private void showBottomListDialog2() {
        final ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add("test" + i);
        }
        TalpaOssdkListBottomDialog.showDialog(this, items, this.getString(R.string.favorites_tab_label), new TalpaOssdkBottomDialog.OnDialogItemSelectListener<String>() {
            @Override
            public void onDialogItemSelect(int index, String chooseItemStr) {
                android.util.Log.i("test", "Click index = " + index + ", chooseItemStr = " + chooseItemStr);
            }
        });
    }

    private void showEditDialg() {
        TalpaOssdkWithEditBottomDialog.showDialog(this,
                this.getString(R.string.favorites_tab_label),
                this.getResources().getString(R.string.favorites_tab_label),
                null, //this.getResources().getString(R.string.favorites_tab_label)
                this.getResources().getString(R.string.cancel),
                this.getResources().getString(R.string.delete),
                new DialogBtnClickListener() {
                    @Override
                    public void onClick() {

                    }
                },
                new DialogBtnClickListener() {
                    @Override
                    public void onClick() {

                    }
                });
    }

    private void showSinglineDialog() {
        TalpaOssdkContentBottomDialog.showDialog(this, null, this.getString(R.string.single_dialog_message),
                this.getString(R.string.cancel), this.getString(R.string.delete), this.getString(R.string.delete),
                null, null, null);
        /*Dialog dialog = new Dialog(SdkDemoApplication.getApplication());
        dialog.setTitle(R.string.app_name);
        dialog.show();*/
    }

    private void showMultiLineDialog() {
        TalpaOssdkContentBottomDialog.showDialog(this, null, this.getString(R.string.multiline_dialog_message),
                this.getString(R.string.cancel), this.getString(R.string.delete),
                null, null);
    }

    private void showMultiLineWithTitleDialog() {
        TalpaOssdkContentBottomDialog.showDialog(this, this.getString(R.string.favorites_tab_label),
                this.getString(R.string.multiline_dialog_message),
                this.getString(R.string.cancel), this.getString(R.string.delete),
                null, null);
    }

    private void showLeftIconDialog() {
        TalpaOssdkContentBottomDialog.showDialog(this, this.getString(R.string.favorites_tab_label),
                this.getString(R.string.multiline_dialog_message), R.drawable.ic_placeholder,
                this.getString(R.string.cancel), this.getString(R.string.delete),
                null, null);
    }

    private void showSingleCheckDialog() {
        TalpaOssdkSingleCheckBottomDialog.showDialog(this, this.getString(R.string.favorites_tab_label),
                this.getString(R.string.multiline_dialog_message), false, this.getResources().getString(R.string.single_check_content), null,
                this.getString(R.string.cancel), this.getString(R.string.delete),
                null, null);
    }
}