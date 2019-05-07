package tran.com.android.gc.lib.preference;

interface OnDependencyChangeListener {
    /**
     * Called when this preference has changed in a way that dependents should
     * care to change their state.
     * 
     * @param disablesDependent Whether the dependent should be disabled.
     */
    void onDependencyChanged(AuroraPreference dependency, boolean disablesDependent);
}
