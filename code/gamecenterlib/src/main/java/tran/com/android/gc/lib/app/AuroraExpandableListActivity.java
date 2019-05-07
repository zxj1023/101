package tran.com.android.gc.lib.app;

import tran.com.android.gc.lib.widget.AuroraExpandableListView;
import tran.com.android.gc.lib.R;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ExpandableListAdapter;

public class AuroraExpandableListActivity extends AuroraActivity implements OnCreateContextMenuListener,
		AuroraExpandableListView.OnChildClickListener, AuroraExpandableListView.OnGroupCollapseListener,
		AuroraExpandableListView.OnGroupExpandListener {
	ExpandableListAdapter mAdapter;
	AuroraExpandableListView mList;
	boolean mFinishedStart = false;

	/**
	 * Override this to populate the context menu when an item is long pressed. menuInfo will contain an
	 * {@link android.widget.AuroraExpandableListView.ExpandableListContextMenuInfo} whose packedPosition is a
	 * packed position that should be used with {@link AuroraExpandableListView#getPackedPositionType(long)} and the
	 * other similar methods.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	}

	/**
	 * Override this for receiving callbacks when a child has been clicked.
	 * <p>
	 * {@inheritDoc}
	 */
	public boolean onChildClick(AuroraExpandableListView parent, View v, int groupPosition, int childPosition,
			long id) {
		return false;
	}

	/**
	 * Override this for receiving callbacks when a group has been collapsed.
	 */
	public void onGroupCollapse(int groupPosition) {
	}

	/**
	 * Override this for receiving callbacks when a group has been expanded.
	 */
	public void onGroupExpand(int groupPosition) {
	}

	/**
	 * Ensures the expandable list view has been created before Activity restores all of the view states.
	 * 
	 * @see Activity#onRestoreInstanceState(Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		ensureList();
		super.onRestoreInstanceState(state);
	}

	/**
	 * Updates the screen state (current list and other views) when the content changes.
	 * 
	 * @see Activity#onContentChanged()
	 */
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		View emptyView = findViewById(android.R.id.empty);
		mList = (AuroraExpandableListView) findViewById(android.R.id.list);
		if (mList == null) {
			throw new RuntimeException("Your content must have a AuroraExpandableListView whose id attribute is "
					+ "'android.R.id.list'");
		}
		if (emptyView != null) {
			mList.setEmptyView(emptyView);
		}
		mList.setOnChildClickListener(this);
		mList.setOnGroupExpandListener(this);
		mList.setOnGroupCollapseListener(this);

		if (mFinishedStart) {
			setListAdapter(mAdapter);
		}
		mFinishedStart = true;
	}

	/**
	 * Provide the adapter for the expandable list.
	 */
	public void setListAdapter(ExpandableListAdapter adapter) {
		synchronized (this) {
			ensureList();
			mAdapter = adapter;
			mList.setAdapter(adapter);
		}
	}

	/**
	 * Get the activity's expandable list view widget. This can be used to get the selection, set the
	 * selection, and many other useful functions.
	 * 
	 * @see AuroraExpandableListView
	 */
	public AuroraExpandableListView getExpandableListView() {
		ensureList();
		return mList;
	}

	/**
	 * Get the ExpandableListAdapter associated with this activity's AuroraExpandableListView.
	 */
	public ExpandableListAdapter getExpandableListAdapter() {
		return mAdapter;
	}

	private void ensureList() {
		if (mList != null) {
			return;
		}
		setContentView(R.layout.aurora_expandable_list_content);
	}

	/**
	 * Gets the ID of the currently selected group or child.
	 * 
	 * @return The ID of the currently selected group or child.
	 */
	public long getSelectedId() {
		return mList.getSelectedId();
	}

	/**
	 * Gets the position (in packed position representation) of the currently selected group or child. Use
	 * {@link AuroraExpandableListView#getPackedPositionType}, {@link AuroraExpandableListView#getPackedPositionGroup},
	 * and {@link AuroraExpandableListView#getPackedPositionChild} to unpack the returned packed position.
	 * 
	 * @return A packed position representation containing the currently selected group or child's position
	 *         and type.
	 */
	public long getSelectedPosition() {
		return mList.getSelectedPosition();
	}

	/**
	 * Sets the selection to the specified child. If the child is in a collapsed group, the group will only be
	 * expanded and child subsequently selected if shouldExpandGroup is set to true, otherwise the method will
	 * return false.
	 * 
	 * @param groupPosition
	 *            The position of the group that contains the child.
	 * @param childPosition
	 *            The position of the child within the group.
	 * @param shouldExpandGroup
	 *            Whether the child's group should be expanded if it is collapsed.
	 * @return Whether the selection was successfully set on the child.
	 */
	public boolean setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
		return mList.setSelectedChild(groupPosition, childPosition, shouldExpandGroup);
	}

	/**
	 * Sets the selection to the specified group.
	 * 
	 * @param groupPosition
	 *            The position of the group that should be selected.
	 */
	public void setSelectedGroup(int groupPosition) {
		mList.setSelectedGroup(groupPosition);
	}

}
