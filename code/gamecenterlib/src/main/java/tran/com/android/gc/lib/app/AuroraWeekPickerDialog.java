
package tran.com.android.gc.lib.app;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tran.com.android.gc.lib.utils.AuroraLog;

import tran.com.android.gc.lib.widget.AuroraNumberPicker;
import tran.com.android.gc.lib.widget.AuroraNumberPicker.OnValueChangeListener;
import tran.com.android.gc.lib.widget.AuroraTimePicker;
import tran.com.android.gc.lib.widget.AuroraTimePicker.OnTimeChangedListener;
import tran.com.android.gc.lib.R;

public class AuroraWeekPickerDialog extends AuroraDialog implements
        android.view.View.OnClickListener {
  
	private static final String LOG_TAG = "AuroraWeekPickerDialog";
	
	
    private static final int WEEK = 1;
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date mDate;

    private AuroraNumberPicker mWeekPicker;
    private AuroraTimePicker mTimePicker;

    private String[] mWeekValues;
    private String[] mShortMonths;
    private String[] mDayValues;
    private String[] mWeekRes;

    private TextView mTitle;

    private Button mPositiveButton;
    private Button mNegativeButton;

    private int mNumberOfMonths;
    private int mNumberOfDay;
    private int mNmberOfNextYearDay;
    private int mWeekNumber;
    private int mHour;
    private int mMinute;
    private int mW;

    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDayTime;

    private int mNeedYear;

    private Locale mCurrentLocale;

    private Calendar mCurrentDate;
    private Calendar mTempDate;
    private Calendar mWeekDate;
    private Calendar mNextDate;

    private onWeekSetListener mCallback;

    private String mDayStr;
    private String mMonthStr;
    private String strTimeFormat;
    private String mCurrentDay;
    private String is24Hours = "24";

    private String mYear;
    private String mMonth;
    private String mDay;
    private String mWeek;

    private String mTempDay;

    private Context mContext;

    private ContentResolver mResolver;

    private View mContentView;

    private String monthTmp;
    private String dayTmp;
    private String weekTmp = "周";

    public interface onWeekSetListener {
        public void onWeekSet(AuroraNumberPicker week, AuroraTimePicker time, String weekValue,
                int hour, int minute);

        public void onWeekSet(Calendar cal);
    }

    public AuroraWeekPickerDialog(Context context, onWeekSetListener callBack, int year, int month,
            int day) {
        this(context, 0, callBack, year, month, day);
    }

    public AuroraWeekPickerDialog(Context context, onWeekSetListener callback,
            Calendar calendar, long minTime) {
        this(context, 0, callback, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        // mTranslateDate = calendar;
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mTimePicker.setCurrentHour(mHour);
        mTimePicker.setCurrentMinute(mMinute);
        mWeek = stringToWeek(calendar);
    }

    private AuroraWeekPickerDialog(Context context, int theme, onWeekSetListener callBack,
            int year, int month, int day) {
        super(context, theme);
        this.mCallback = callBack;
        this.mContext = context;
        monthTmp = mContext.getResources().getString(
                R.string.aurora_datepicker_month_lebel);
        dayTmp = mContext.getResources().getString(
                R.string.aurora_datepicker_day_label);
        mContentView = LayoutInflater.from(mContext).inflate(
                R.layout.aurora_week_picker_dialog, null);
        mWeekRes = context.getResources().getStringArray(R.array.auroraWeekNumber);
        mDayStr = context.getResources().getString(
                R.string.aurora_datepicker_day_label);
        mMonthStr = context.getResources().getString(
                R.string.aurora_datepicker_month_lebel);
        mResolver = mContext.getContentResolver();
        strTimeFormat = android.provider.Settings.System.getString(mResolver,
                android.provider.Settings.System.TIME_12_24);
        initCalendars();
        final int currentYear = year;
        mNeedYear = year;
        mCurrentDate.set(year, month, day);
        mTempDate.set(year + 1, month, day);
        mCurrentMonth = month + 1;
        mCurrentDayTime = day;
        mHour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mCurrentDate.get(Calendar.MINUTE);
        OnValueChangeListener valueChangeListener = new OnValueChangeListener() {

            @Override
            public void onValueChange(AuroraNumberPicker picker, int oldVal, int newVal) {
                if ((oldVal == mNumberOfDay - 1) && (newVal == mNumberOfDay)) {
                    mNeedYear += 1;
                } else if ((oldVal == mNumberOfDay) && (newVal == mNumberOfDay - 1)) {
                    mNeedYear -= 1;
                }
                updateTitle(mWeekValues[newVal]);
                setCurrentWeek(newVal);
            }
        };

        initView();
        mWeekPicker.setOnValueChangedListener(valueChangeListener);
        updateTitle(year, month + 1, day);

    }

    @Override
    public void showInternal(boolean hideView) {
    	// TODO Auto-generated method stub
    	super.showInternal(hideView);
    	View content = mWindow.findViewById(R.id.aurora_time_picker_layout);
    	doTouchOutsideEvents(content, mWindow.getDecorView());
    }
    
    /**
     * @return The selected year.
     */
    public int getYear() {
        return mCurrentDate.get(Calendar.YEAR);
    }

    /**
     * @return The selected month.
     */
    public int getMonth() {
        return mCurrentDate.get(Calendar.MONTH);
    }

    /**
     * @return The selected day of month.
     */
    public int getDayOfMonth() {
        return mCurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    private void initCalendars() {
        mCurrentDate = Calendar.getInstance();
        mTempDate = Calendar.getInstance();
        mNextDate = Calendar.getInstance();
        mWeekDate = Calendar.getInstance();
    }

    private void setDate(int year, int month, int day) {
        mCurrentDate.set(year, month, day);
    }

    private void updateSpinners() {
        mWeekPicker.setMinValue(0);
        mWeekPicker.setMaxValue(mCurrentDate.getActualMaximum(Calendar.DAY_OF_YEAR) - 1);
        getWeek();
        mWeekPicker.setDisplayedValues(mWeekValues);

    }

    private void notifyDataChanged() {
        if (mCallback != null) {
            mCallback.onWeekSet(mCurrentDate);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(mContentView);

    }

    private boolean isLocale() {
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals("zh")) {
            return true;
        }
        return false;
    }

    /**
     * Gets a calendar for locale bootstrapped with the value of a given
     * calendar.
     * 
     * @param oldCalendar The old calendar.
     * @param locale The locale.
     */
    private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
        if (oldCalendar == null) {
            return Calendar.getInstance(locale);
        } else {
            final long currentTimeMillis = oldCalendar.getTimeInMillis();
            Calendar newCalendar = Calendar.getInstance(locale);
            newCalendar.setTimeInMillis(currentTimeMillis);
            return newCalendar;
        }
    }

    private void initView() {
        mWeekPicker = (AuroraNumberPicker) mContentView
                .findViewById(R.id.aurora_week);
        mTimePicker = (AuroraTimePicker) mContentView
                .findViewById(R.id.aurora_time_picker);

        mPositiveButton = (Button) mContentView
                .findViewById(R.id.aurora_date_picker_done_button);
        mNegativeButton = (Button) mContentView
                .findViewById(R.id.aurora_date_picker_cancel_button);

        mTitle = (TextView) mContentView
                .findViewById(R.id.aurora_date_picker_title);
        mPositiveButton.setOnClickListener(this);
        mNegativeButton.setOnClickListener(this);
        initWeekPicker();
        initTimePicker();
    }

    private void initWeekPicker() {
        int selectedValue = 0;
        getWeek();
        mWeekPicker.setMinValue(0);
        mWeekPicker.setTextSize(19.0f);
        mWeekPicker.setMaxValue(mWeekValues.length - 1);
        mWeekPicker.setSelectionSrc(mContext.getResources().getDrawable(
                R.drawable.aurora_numberpicker_selector));
        mWeekPicker.setDisplayedValues(mWeekValues);
        mWeekPicker.setSelectionSrc(mContext.getResources().getDrawable(
                R.drawable.aurora_numberpicker_selector));
        mWeekPicker.setTextSize(19.0f);
        mWeekPicker.setWrapSelectorWheel(false);
        for (int i = 0; i < mNumberOfDay; i++) {
            String str = mWeekValues[i];
            String month = "";
            if (isLocale()) {
                month = mCurrentMonth + "";
                if (str.substring(0, 2).contains(month) &&
                        str.substring(2, str.length()).contains(mCurrentDayTime + "")) {
                    selectedValue = i;
                    break;
                }
            } else {
                month = DateUtils.getMonthString(mCurrentMonth - 1, DateUtils.LENGTH_MEDIUM);
                if (str.contains(month) &&
                        str.contains(mCurrentDayTime + "")) {
                    selectedValue = i;
                    break;
                }

            }
            continue;
        }
        mWeekPicker.setValue(selectedValue);
        mWeek = mWeekValues[mWeekPicker.getValue()];
    }

    private void updateTitle(int year, int month, int day) {
        if (isLocale()) {
            mTitle.setText(mCurrentDay);
            if ((year <= 0) || (month <= 0) || (day <= 0)) {
                return;
            }
            mCurrentDay = year + "-" + month + "-" + day;
            mTitle.setText(mCurrentDay);
        } else {
            mTitle.setText(mCurrentDay);
            if ((year <= 0) || (month <= 0) || (day <= 0)) {
                return;
            }
            mCurrentDay = year + " " + DateUtils.getMonthString(month - 1, DateUtils.LENGTH_MEDIUM)
                    + " " + day;
            mTitle.setText(mCurrentDay);
        }
    }

    private void updateTitle(String week) {
        if (isLocale()) {
            String[] tempWeek = week.split(weekTmp);
            String date = tempWeek[0];
            String str = date.replace(dayTmp, "");
            str = str.replace(monthTmp, "-");
            mCurrentDay = mNeedYear + "-" + str;
            mTitle.setText(mCurrentDay);
        } else {
            String[] tempWeek = week.split(" ");
            String date = tempWeek[0] + " " + tempWeek[1];
            mCurrentDay = date + " " + mNeedYear;
            mTitle.setText(mCurrentDay);
        }
    }

    private int getDay(String week) {
        int day = 0;
        if (isLocale()) {
            String[] tempWeek = week.split(weekTmp);
            String date = tempWeek[0];
            String str = date.replace(dayTmp, "");
            str = str.replace(monthTmp, "-");
            String[] tempDay = str.split("-");
            day = Integer.parseInt(tempDay[1]);
        } else {
            String[] tempWeek = week.split(" ");
            day = Integer.parseInt(tempWeek[1]);
        }
        return day;

    }

    private int getMonth(String week) {
        int month = 0;
        if (isLocale()) {
            String[] tempWeek = week.split(weekTmp);
            String date = tempWeek[0];
            String str = date.replace(dayTmp, "");
            str = str.replace(monthTmp, "-");
            String[] tempDay = str.split("-");
            month = Integer.parseInt(tempDay[0]);
        } else {
            String[] tempWeek = week.split(" ");
            AuroraLog.e(LOG_TAG, "tempWeek[0]:"+tempWeek[0]);
            month = getMonthByDigit(tempWeek[0]);
        }
        return month;
    }

    private void getWeek() {
        mNmberOfNextYearDay = mTempDate.getActualMaximum(Calendar.DAY_OF_YEAR);

        mNumberOfDay = mCurrentDate.getActualMaximum(Calendar.DAY_OF_YEAR);

        mWeekValues = new String[mNumberOfDay + mNmberOfNextYearDay];

        int day = 0;
        for (int i = 0; i < mNumberOfDay; i++) {
            mWeekDate.set(Calendar.YEAR, mCurrentDate.get(Calendar.YEAR));
            mWeekDate.set(Calendar.DAY_OF_YEAR, i + 1);
            mWeekValues[i] = stringToWeek();
        }
        for (int i = mNumberOfDay - 1; i < mWeekValues.length; i++) {
            mWeekDate.set(Calendar.YEAR, mTempDate.get(Calendar.YEAR));
            mWeekDate.set(Calendar.DAY_OF_YEAR, day++);
            mWeekValues[i] = stringToWeek();
        }

    }

    private String stringToWeek() {
        Locale currentLocal = Locale.getDefault();
        StringBuilder builder = null;
        if (currentLocal.getLanguage().equals("zh")) {
            builder = new StringBuilder();
            mYear = String.valueOf(mWeekDate.get(Calendar.YEAR));
            mMonth = String.valueOf(mWeekDate.get(Calendar.MONTH) + 1);
            mDay = String.valueOf(mWeekDate.get(Calendar.DAY_OF_MONTH));
            mW = mWeekDate.get(Calendar.DAY_OF_WEEK);
            if (WEEK == mW) {
                mWeek = mWeekRes[0];
            } else if (WEEK + 1 == mW) {
                mWeek = mWeekRes[1];
            } else if (WEEK + 2 == mW) {
                mWeek = mWeekRes[2];
            } else if (WEEK + 3 == mW) {
                mWeek = mWeekRes[3];
            } else if (WEEK + 4 == mW) {
                mWeek = mWeekRes[4];
            } else if (WEEK + 5 == mW) {
                mWeek = mWeekRes[5];
            } else if (WEEK + 6 == mW) {
                mWeek = mWeekRes[6];
            }
            builder.append(mMonth);
            builder.append(mMonthStr);
            builder.append(mDay);
            builder.append(mDayStr);
            builder.append(mWeek);
            return builder.toString();
        } else {
            builder = new StringBuilder();
            mYear = String.valueOf(mWeekDate.get(Calendar.YEAR));
            mMonth = DateUtils.getMonthString(mWeekDate.get(Calendar.MONTH),
                    DateUtils.LENGTH_MEDIUM);
            mDay = String.valueOf(mWeekDate.get(Calendar.DAY_OF_MONTH));
            mW = mWeekDate.get(Calendar.DAY_OF_WEEK);
            if (WEEK == mW) {
                mWeek = mWeekRes[0];
            } else if (WEEK + 1 == mW) {
                mWeek = mWeekRes[1];
            } else if (WEEK + 2 == mW) {
                mWeek = mWeekRes[2];
            } else if (WEEK + 3 == mW) {
                mWeek = mWeekRes[3];
            } else if (WEEK + 4 == mW) {
                mWeek = mWeekRes[4];
            } else if (WEEK + 5 == mW) {
                mWeek = mWeekRes[5];
            } else if (WEEK + 6 == mW) {
                mWeek = mWeekRes[6];
            }
            builder.append(mMonth);
            builder.append(" ");
            builder.append(mDay);
            builder.append(" ");
            builder.append(mWeek);
            return builder.toString();
        }
    }

    private String stringToWeek(Calendar c) {
        String year = "";
        String month = "";
        String day = "";
        String week = "";
        Locale currentLocal = Locale.getDefault();
        if (currentLocal.getLanguage().equals("zh")) {
            year = String.valueOf(c.get(Calendar.YEAR));
            month = String.valueOf(c.get(Calendar.MONTH) + 1);
            day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            int W = c.get(Calendar.DAY_OF_WEEK);

            if (WEEK == W) {
                week = mWeekRes[0];
            } else if (WEEK + 1 == W) {
                week = mWeekRes[1];
            } else if (WEEK + 2 == W) {
                week = mWeekRes[2];
            } else if (WEEK + 3 == W) {
                week = mWeekRes[3];
            } else if (WEEK + 4 == W) {
                week = mWeekRes[4];
            } else if (WEEK + 5 == W) {
                week = mWeekRes[5];
            } else if (WEEK + 6 == W) {
                week = mWeekRes[6];
            }
            return month + mMonthStr + day + mDayStr + week;
        } else {
            // year = String.valueOf(c.get(Calendar.YEAR));
            month = DateUtils.getMonthString(c.get(Calendar.MONTH) , DateUtils.LENGTH_MEDIUM);
            day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            // c.get(Calendar.DAY_OF_MONTH);//String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            int W = c.get(Calendar.DAY_OF_WEEK);

            if (WEEK == W) {
                week = mWeekRes[0];
            } else if (WEEK + 1 == W) {
                week = mWeekRes[1];
            } else if (WEEK + 2 == W) {
                week = mWeekRes[2];
            } else if (WEEK + 3 == W) {
                week = mWeekRes[3];
            } else if (WEEK + 4 == W) {
                week = mWeekRes[4];
            } else if (WEEK + 5 == W) {
                week = mWeekRes[5];
            } else if (WEEK + 6 == W) {
                week = mWeekRes[6];
            }
            return month + " " + day + " " + week;
        }

    }

    private int getCurrentDate() {
        mYear = String.valueOf(mCurrentDate.get(Calendar.YEAR));
        mMonth = String.valueOf(mCurrentDate.get(Calendar.MONTH) + 1);
        mDay = String.valueOf(mCurrentDate.get(Calendar.DAY_OF_MONTH));
        int w = mTempDate.get(Calendar.DAY_OF_WEEK);
        return w;
    }

    private void setSelectedValue() {
        getCurrentDate();
    }

    private void initTimePicker() {
        mTimePicker.setTextSize(19.0f);
        mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

            @Override
            public void onTimeChanged(AuroraTimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                setCurrentHour(hourOfDay);
                setCurrentMinute(minute);
            }
        });
        // if(strTimeFormat.equals(is24Hours)){
        mTimePicker.setIs24HourView(true);
        // }else{
        // mTimePicker.setIs24HourView(false);
        // }
    }

    public void setOnWeekSetListener(onWeekSetListener listener) {
        this.mCallback = listener;
    }

    private void setCurrentWeek(int week) {
        mWeekPicker.setValue(week);
        mWeek = mWeekValues[week];
    }

    public void setCurrentDate(int year, int month, int day) {
        mCurrentYear = year;
        mCurrentMonth = month;
        mCurrentDayTime = day;
        mCurrentDate.set(mCurrentYear, mCurrentMonth - 1, mCurrentDayTime);
        mTempDay = mCurrentMonth + mMonthStr + mCurrentDayTime + mDayStr;
        updateTitle(year, month, day);
        initWeekPicker();
    }

    public void setCurrentHour(int currentHour) {
        mTimePicker.setCurrentHour(currentHour);
    }

    public void setCurrentMinute(int currentMinute) {
        mTimePicker.setCurrentMinute(currentMinute);
    }

    public String getCurrentWeek() {

        return mWeek;
    }

    public int getCurrentHour() {
        return mTimePicker.getCurrentHour();
    }

    public int getCurrentMinute() {
        return mTimePicker.getCurrentMinute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int i = v.getId();
        if (i == R.id.aurora_date_picker_done_button) {
            dealData();

        } else if (i == R.id.aurora_date_picker_cancel_button) {
            dismiss();

        } else {
        }

    }

    private void dealData() {
        if (mCallback != null) {
            mCallback.onWeekSet(mWeekPicker, mTimePicker, getCurrentWeek(), getCurrentHour(),
                    getCurrentMinute());

            Calendar c = Calendar.getInstance();
            c.clear();
           
            int month = getMonth(getCurrentWeek());
            AuroraLog.e(LOG_TAG, "month:"+month);
            c.set(mNeedYear, month-1,
                    getDay(getCurrentWeek()),
                    getCurrentHour(), getCurrentMinute());
            mCallback.onWeekSet(c);
        }
        dismiss();
    }

    private int getMonthByDigit(String month) {

        int m = 1;
        if ("Jan".equals(month)) {
            m = 1;
        } else if ("Feb".endsWith(month)) {
            m = 2;
        } else if ("Mar".endsWith(month)) {
            m = 3;
        }
        else if ("Apr".endsWith(month)) {
            m = 4;
        }
        else if ("May".endsWith(month)) {
            m = 5;
        }
        else if ("Jun".endsWith(month)) {
            m = 6;
        }
        else if ("Jul".endsWith(month)) {
            m = 7;
        }
        else if ("Aug".endsWith(month)) {
            m = 8;
        }
        else if ("Sep".endsWith(month)) {
            m = 9;
        }
        else if ("Oct".endsWith(month)) {
            m = 10;
        }
        else if ("Nov".endsWith(month)) {
            m = 11;
        }
        else if ("Dec".endsWith(month)) {
            m = 12;
        }
        return m;
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

}
