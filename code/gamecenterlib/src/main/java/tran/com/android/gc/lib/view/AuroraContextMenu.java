package tran.com.android.gc.lib.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;

public interface AuroraContextMenu extends AuroraMenu{
	/**
     * Sets the context menu header's title to the title given in <var>titleRes</var>
     * resource identifier.
     * 
     * @param titleRes The string resource identifier used for the title.
     * @return This AuroraContextMenu so additional setters can be called.
     */
    public AuroraContextMenu setHeaderTitle(int titleRes);

    /**
     * Sets the context menu header's title to the title given in <var>title</var>.
     * 
     * @param title The character sequence used for the title.
     * @return This AuroraContextMenu so additional setters can be called.
     */
    public AuroraContextMenu setHeaderTitle(CharSequence title);
    
    /**
     * Sets the context menu header's icon to the icon given in <var>iconRes</var>
     * resource id.
     * 
     * @param iconRes The resource identifier used for the icon.
     * @return This AuroraContextMenu so additional setters can be called.
     */
    public AuroraContextMenu setHeaderIcon(int iconRes);

    /**
     * Sets the context menu header's icon to the icon given in <var>icon</var>
     * {@link Drawable}.
     * 
     * @param icon The {@link Drawable} used for the icon.
     * @return This AuroraContextMenu so additional setters can be called.
     */
    public AuroraContextMenu setHeaderIcon(Drawable icon);
    
    /**
     * Sets the header of the context menu to the {@link View} given in
     * <var>view</var>. This replaces the header title and icon (and those
     * replace this).
     * 
     * @param view The {@link View} used for the header.
     * @return This AuroraContextMenu so additional setters can be called.
     */
    public AuroraContextMenu setHeaderView(View view);
    
    /**
     * Clears the header of the context menu.
     */
    public void clearHeader();
    
    /**
     * Additional information regarding the creation of the context menu.  For example,
     * {@link AdapterView}s use this to pass the exact item position within the adapter
     * that initiated the context menu.
     */
    public interface ContextMenuInfo {
    }

}
