package tran.com.android.gc.lib.widget;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tran.com.android.gc.lib.R;


public class AuroraInputMethodListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<String> mMethods;
	public AuroraInputMethodListAdapter(Context context){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}
	public void setMethods(ArrayList<String> list){
		this.mMethods = list;
	}
	public void updateMethods(){
		notifyDataSetChanged();
	}
	public void addMethod(String name){
		if(mMethods.contains(name)){
			return;
		}
		mMethods.add(name);
		notifyDataSetChanged();
	}
	public void getMethodId(){
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (mMethods == null || mMethods.size() == 0)?0:mMethods.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return (mMethods == null || mMethods.size() == 0)?null:mMethods.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.aurora_input_method_list_item, null);
			holder = new Holder();
			holder.methodName = (TextView)convertView.findViewById(R.id.aurora_input_method_name);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}
		holder.methodName.setText(mMethods.get(position));
		return convertView;
	}
	
	class Holder{
		TextView methodName;
	}
	
	
	
	
	
	
	

}
