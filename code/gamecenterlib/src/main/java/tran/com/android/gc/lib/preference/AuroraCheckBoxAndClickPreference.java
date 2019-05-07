package tran.com.android.gc.lib.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import tran.com.android.gc.lib.R;


public class AuroraCheckBoxAndClickPreference extends AuroraCheckBoxPreference {

	private CheckBox mchCheckBox;
	private OnClickListener mListener;

	public AuroraCheckBoxAndClickPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayoutResource(R.layout.aurora_preference_check_click);
		setWidgetLayoutResource(R.layout.aurora_preference_widget_checkbox);
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		View layout = view.findViewById(R.id.aurora_check_click_pref);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mListener != null) {
					mListener.onClick(arg0);
				}
			}
		});

		mchCheckBox = (CheckBox) view.findViewById(android.R.id.checkbox);
		mchCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBoxClicked();
			}
		});
	}

	protected void checkBoxClicked() {
		if (isChecked()) {
			setChecked(false);
		} else {
			setChecked(true);
		}
	}

	public void setOnDetailClickListener(OnClickListener listener) {
		this.mListener = listener;
	}

}