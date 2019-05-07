package tran.com.android.gc.lib.view;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.view.View;

public interface AuroraSubMenu extends AuroraMenu {
	/**
	 * Sets the AuroraSubMenu header's title to the title given in <var>titleRes</var> resource identifier.
	 * 
	 * @param titleRes
	 *            The string resource identifier used for the title.
	 * @return This AuroraSubMenu so additional setters can be called.
	 */
	public AuroraSubMenu setHeaderTitle(int titleRes);

	/**
	 * Sets the AuroraSubMenu header's title to the title given in <var>title</var>.
	 * 
	 * @param title
	 *            The character sequence used for the title.
	 * @return This AuroraSubMenu so additional setters can be called.
	 */
	public AuroraSubMenu setHeaderTitle(CharSequence title);

	/**
	 * Sets the AuroraSubMenu header's icon to the icon given in <var>iconRes</var> resource id.
	 * 
	 * @param iconRes
	 *            The resource identifier used for the icon.
	 * @return This AuroraSubMenu so additional setters can be called.
	 */
	public AuroraSubMenu setHeaderIcon(int iconRes);

	/**
	 * Sets the AuroraSubMenu header's icon to the icon given in <var>icon</var> {@link Drawable}.
	 * 
	 * @param icon
	 *            The {@link Drawable} used for the icon.
	 * @return This AuroraSubMenu so additional setters can be called.
	 */
	public AuroraSubMenu setHeaderIcon(Drawable icon);

	/**
	 * Sets the header of the AuroraSubMenu to the {@link View} given in <var>view</var>. This replaces the header
	 * title and icon (and those replace this).
	 * 
	 * @param view
	 *            The {@link View} used for the header.
	 * @return This AuroraSubMenu so additional setters can be called.
	 */
	public AuroraSubMenu setHeaderView(View view);

	/**
	 * Clears the header of the AuroraSubMenu.
	 */
	public void clearHeader();

	/**
	 * Change the icon associated with this AuroraSubMenu's item in its parent menu.
	 * 
	 * @see MenuItem#setIcon(int)
	 * @param iconRes
	 *            The new icon (as a resource ID) to be displayed.
	 * @return This AuroraSubMenu so additional setters can be called.
	 */
	public AuroraSubMenu setIcon(int iconRes);

	/**
	 * Change the icon associated with this AuroraSubMenu's item in its parent menu.
	 * 
	 * @see MenuItem#setIcon(Drawable)
	 * @param icon
	 *            The new icon (as a Drawable) to be displayed.
	 * @return This AuroraSubMenu so additional setters can be called.
	 */
	public AuroraSubMenu setIcon(Drawable icon);

	/**
	 * Gets the {@link MenuItem} that represents this AuroraSubMenu in the parent menu. Use this for setting
	 * additional item attributes.
	 * 
	 * @return The {@link MenuItem} that launches the AuroraSubMenu when invoked.
	 */
	public AuroraMenuItem getItem();

}
