package com.app.android.mapgeneral.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//import com.firebase.client.AuthData;
//import com.firebase.client.Firebase;

import com.app.android.mapgeneral.MenuData;
import com.app.android.mapgeneral.Fragments.MenuDetailViewFragment;
import com.app.android.mapgeneral.Objects.Shared;
import com.app.android.mapgeneral.Objects.TextDrawable;
import com.app.android.mapgeneral.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MenuPagerActivity extends AppCompatActivity implements MenuDetailViewFragment.CheckChangedListener
{
  private final static String TAG = "MenuPagerActivity";
  private ViewPager mViewPager;
  private MenuData menuData = new MenuData();
  private Menu mainMenu;
  private ArrayList<String> cartItems = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_viewpager);

    Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_pager);
    setSupportActionBar(mToolbar);

    Intent intent = getIntent();
    HashMap<String, List<HashMap<String, String>>> map = (HashMap<String, List<HashMap<String, String>>>) intent.getSerializableExtra("menu");
    HashMap<String, List<HashMap<String, String>>> mMap = menuData.getMenu();
    mMap.clear();
    if (map != null) for (String key : map.keySet()) mMap.put(key, map.get(key));

    mViewPager = (ViewPager) findViewById(R.id.pager);
    MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), menuData);

    mViewPager.setAdapter(mPagerAdapter);
    mViewPager.setCurrentItem(0);
    customizeViewPager();
    final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    if (mPagerAdapter.getCount() > 0) tabLayout.post(new Runnable()
      {
        @Override
        public void run()
        {tabLayout.setupWithViewPager(mViewPager);}
      });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    switch (id)
    {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menudetail_menu, menu);
    this.mainMenu = menu;
    return true;
  }

  private void customizeViewPager()
  {
    mViewPager.setPageTransformer(false, new ViewPager.PageTransformer()
    {
      @Override
      public void transformPage(View page, float position)
      {
        final float normalized_position = Math.abs(Math.abs(position) - 1);
        page.setScaleX(normalized_position / 2 + 0.5f);
        page.setScaleY(normalized_position / 2 + 0.5f);
      }
    });

    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
      {

      }

      @Override
      public void onPageSelected(int position)
      {

      }

      @Override
      public void onPageScrollStateChanged(int state)
      {

      }
    });
  }

  //Add items to cart
  @Override
  public void onCheckChanged(boolean isChecked, String apiKey)
  {
    logMessage(apiKey+" is Checked? " +isChecked);
    if (cartItems.contains(apiKey) && !isChecked) cartItems.remove(apiKey);
    else if (!cartItems.contains(apiKey) && isChecked) cartItems.add(apiKey);
    logMessage("Total items in the cart: "+cartItems.size());
    MenuItem cartItem = mainMenu.findItem(R.id.add_cart);
    Drawable cartLayers[] = new Drawable[2];
    cartLayers[0]  = getDrawable(R.drawable.ic_shopping_cart_black_24dp);
    cartLayers[1]  = (new TextDrawable(cartItems.size()+""));//getDrawable(R.drawable.ic_shopping_cart_black_24dp);
    LayerDrawable cartIconImage = new LayerDrawable(cartLayers);
    if (cartItems.size() > 0) cartItem.setIcon(cartIconImage);
    else cartItem.setIcon(cartLayers[0]);
  }

  public void logMessage(String msg) {if (Shared.DEBUG_MODE) Log.i(TAG, msg);}

}

class MyPagerAdapter extends FragmentPagerAdapter
{
  private MenuData mMenuData;

  public MyPagerAdapter(FragmentManager fm, MenuData menuData)
  {
    super(fm);
    mMenuData = menuData;
  }

  @Override
  public Fragment getItem(int position)
  {
    String name = mMenuData.getMenuItem(position);
    List<HashMap<String, String>> map = mMenuData.getMenu().get(name);
    return MenuDetailViewFragment.newInstance(map);
  }

  @Override
  public int getCount()
  {
    return mMenuData.getSize();
  }

  @Override
  public CharSequence getPageTitle(int position)
  {
    Locale l = Locale.getDefault();
    String name = mMenuData.getMenuItem(position);
    return name.toUpperCase(l);
  }
}
