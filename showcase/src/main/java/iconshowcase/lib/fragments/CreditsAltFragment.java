package iconshowcase.lib.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import iconshowcase.lib.R;
import iconshowcase.lib.dialogs.ISDialogs;
import iconshowcase.lib.utilities.Preferences;
import iconshowcase.lib.utilities.ThemeUtils;
import iconshowcase.lib.utilities.Utils;

public class CreditsAltFragment extends Fragment {

    private Context context;
    private ViewGroup layout;
    private Preferences mPrefs;

    private boolean YOU_HAVE_WEBSITE = false;

    String[] libsLinks, contributorsLinks, uiCollaboratorsLinks, designerLinks, translatorsLinks;

    Drawable collaboratorsIcon, libs, uiCollaboratorsIcon, sherryIcon, translators;
    ImageView uiCollaboratorsIV, collaboratorsIV, libsIcon, sherryIV, translatorsIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        mPrefs = new Preferences(getActivity());

        libsLinks = context.getResources().getStringArray(R.array.libs_links);
        contributorsLinks = context.getResources().getStringArray(R.array.contributors_links);
        uiCollaboratorsLinks = context.getResources().getStringArray(R.array.ui_collaborators_links);
        designerLinks = context.getResources().getStringArray(R.array.iconpack_author_links);
        translatorsLinks = context.getResources().getStringArray(R.array.translators_links);

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }

        layout = (ViewGroup) inflater.inflate(R.layout.credits_section_alt, container, false);

        setupViews(layout);

        ImageView devBanner = (ImageView) layout.findViewById(R.id.developerHeader);
        Glide.with(context)
                .load(Utils.getStringFromResources(context, R.string.dashboard_author_banner))
                .centerCrop()
                .into(devBanner);

        ImageView devPhoto = (ImageView) layout.findViewById(R.id.developerPhoto);

        Glide.with(context)
                .load(Utils.getStringFromResources(context, R.string.dashboard_author_photo))
                .into(devPhoto);

        ImageView designerBanner = (ImageView) layout.findViewById(R.id.designerHeader);
        Glide.with(context)
                .load(Utils.getStringFromResources(context, R.string.iconpack_author_banner))
                .centerCrop()
                .into(designerBanner);

        ImageView designerPhoto = (ImageView) layout.findViewById(R.id.designerPhoto);
        Glide.with(context)
                .load(Utils.getStringFromResources(context, R.string.iconpack_author_photo))
                .into(designerPhoto);

        CardView sherryCV = (CardView) layout.findViewById(R.id.sherryCard);
        sherryCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showSherryDialog(context);
            }
        });

        CardView contributorsCV = (CardView) layout.findViewById(R.id.contributorsCard);
        contributorsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showContributorsDialog(context, contributorsLinks);
            }
        });

        CardView uiCollabs = (CardView) layout.findViewById(R.id.uiDesignCard);
        uiCollabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showUICollaboratorsDialog(context, uiCollaboratorsLinks);
            }

        });

        CardView libsCard = (CardView) layout.findViewById(R.id.libsCard);
        libsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showLibrariesDialog(context, libsLinks);
            }

        });

        CardView translators = (CardView) layout.findViewById(R.id.translatorsCard);
        translators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISDialogs.showTranslatorsDialogs(context, translatorsLinks);
            }

        });

        return layout;
    }

    private void setupViews(final ViewGroup layout) {

        final int light = ContextCompat.getColor(context, android.R.color.white);
        final int dark = ContextCompat.getColor(context, R.color.grey);

        collaboratorsIcon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_code)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        libs = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_file_text)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        sherryIcon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_star)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        uiCollaboratorsIcon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_palette)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        translators = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_translate)
                .color(ThemeUtils.darkTheme ? light : dark)
                .sizeDp(24);

        libsIcon = (ImageView) layout.findViewById(R.id.icon_libs);
        collaboratorsIV = (ImageView) layout.findViewById(R.id.icon_collaborators);
        sherryIV = (ImageView) layout.findViewById(R.id.icon_sherry);
        uiCollaboratorsIV = (ImageView) layout.findViewById(R.id.icon_ui_design);
        translatorsIV = (ImageView) layout.findViewById(R.id.icon_translators);

        libsIcon.setImageDrawable(libs);
        collaboratorsIV.setImageDrawable(collaboratorsIcon);
        sherryIV.setImageDrawable(sherryIcon);
        uiCollaboratorsIV.setImageDrawable(uiCollaboratorsIcon);
        translatorsIV.setImageDrawable(translators);

        AppCompatButton emailBtn = (AppCompatButton) layout.findViewById(R.id.send_email_btn);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendEmailWithDeviceInfo(context);
            }
        });

        AppCompatButton websiteBtn = (AppCompatButton) layout.findViewById(R.id.website_btn);
        if (YOU_HAVE_WEBSITE) {
            websiteBtn.setText(Utils.getStringFromResources(context, R.string.visit_website));
        } else {
            websiteBtn.setText(Utils.getStringFromResources(context, R.string.more));
        }
        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YOU_HAVE_WEBSITE) {
                    Utils.openLinkInChromeCustomTab(context,
                            getResources().getString(R.string.iconpack_author_website));
                } else {
                    ISDialogs.showDesignerLinksDialog(context, designerLinks);
                }
            }
        });

        AppCompatButton googleBtn = (AppCompatButton) layout.findViewById(R.id.googleplus_btn);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.iconpack_author_gplus));
            }
        });

        AppCompatButton forkBtn = (AppCompatButton) layout.findViewById(R.id.fork_btn);
        forkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.dashboard_author_github));
            }
        });

        AppCompatButton devWebsiteBtn = (AppCompatButton) layout.findViewById(R.id.dev_website_btn);
        devWebsiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.dashboard_author_website));
            }
        });

        AppCompatButton devGoogleBtn = (AppCompatButton) layout.findViewById(R.id.dev_googleplus_btn);
        devGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openLinkInChromeCustomTab(context,
                        getResources().getString(R.string.dashboard_author_gplus_community));
            }
        });

    }

}