/*
 * Copyright (c) 2016.  Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Big thanks to the project contributors. Check them in the repository.
 *
 */

/*
 *
 */

package jahirfiquitiva.iconshowcase.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pluscubed.recyclerfastscroll.RecyclerFastScroller;

import jahirfiquitiva.iconshowcase.R;
import jahirfiquitiva.iconshowcase.activities.ShowcaseActivity;
import jahirfiquitiva.iconshowcase.adapters.ZooperAdapter;
import jahirfiquitiva.iconshowcase.tasks.LoadZooperWidgets;
import jahirfiquitiva.iconshowcase.utilities.Utils;
import jahirfiquitiva.iconshowcase.views.GridSpacingItemDecoration;

public class ZooperFragment extends Fragment {

    private static ViewGroup layout;
    private static Context context;

    public RecyclerView mRecyclerView;
    private RecyclerFastScroller fastScroller;
    public ZooperAdapter zooperAdapter;
    private int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        int gridSpacing = getResources().getDimensionPixelSize(R.dimen.lists_padding);
        int columnsNumber = getResources().getInteger(R.integer.launchers_grid_width) - 1;

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        try {
            layout = (ViewGroup) inflater.inflate(R.layout.zooper_section, container, false);
        } catch (InflateException e) {
            //Do nothing
        }

        if (layout != null) {

            mRecyclerView = (RecyclerView) layout.findViewById(R.id.zooper_rv);
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, columnsNumber));
            mRecyclerView.addItemDecoration(
                    new GridSpacingItemDecoration(columnsNumber,
                            gridSpacing,
                            true));

            fastScroller = (RecyclerFastScroller) layout.findViewById(R.id.rvFastScroller);
            fastScroller.attachRecyclerView(mRecyclerView);

            zooperAdapter = new ZooperAdapter(context, LoadZooperWidgets.widgets,
                    ShowcaseActivity.wallpaperDrawable);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(zooperAdapter);

        }

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.collapseToolbar(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static void showInstalledAppsSnackbar() {
        if (layout != null && context != null) {
            Utils.showSimpleSnackbar(context, layout,
                    Utils.getStringFromResources(context, R.string.apps_installed));
        }
    }

    public static void showInstalledAssetsSnackbar() {
        if (layout != null && context != null) {
            Utils.showSimpleSnackbar(context, layout,
                    Utils.getStringFromResources(context, R.string.assets_installed));
        }
    }

}