package com.example.shareinuapp.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.shareinuapp.LockerExplainActivity;
import com.example.shareinuapp.MainActivity;
import com.example.shareinuapp.PostActivity;
import com.example.shareinuapp.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.support.v7.app.ActionBar;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class FirstFragment extends Fragment{ // 첫 번째 fragment
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewPager mViewPager;
    private ViewPager mViewPager2;
    FragmentPagerAdapter adapterViewPager;
    private List<Integer> imageIdList;
    private Timer swipeTimer;
    private int NUM_PAGES = 3; // 광고 페이지 개수
    private int currentPage = 0; // 현재 페이지
    private ImageButton imageButton;
    private ImageButton mPagerListener1;

//    private ChildFragment1 childFragment1;
//    private ChildFragment2 childFragment2;

    public FirstFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public interface CustomOnClickListener {
        public void onClicked( View v );
    }

    private CustomOnClickListener customOnClickListener;

    public void buttonClicked( View v ) {
        customOnClickListener.onClicked(v);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    // first_Fragment가 화면으로 나타내는 것
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        childFragment1 = new ChildFragment1();
//        childFragment2 = new ChildFragment2();

//        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.hometopbar));
//        actionBar.getCustomView(R.layout.layout_actionbar);
//        View actionbar = inflater.inflate(R.layout.layout_actionbar, null);
//        actionBar.setCustomView(actionbar);
//
//        actionBar.setDisplayHomeAsUpEnabled(false);

        View v = inflater.inflate(com.example.shareinuapp.R.layout.fragment_first, container, false);

//        View mCustomView = inflater.inflate(R.layout.layout_actionbar, container,false);
//        actionBar.setCustomView(mCustomView);
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.hometopbar));
//        actionBar.setDisplayHomeAsUpEnabled(false);

        mViewPager = (ViewPager) v.findViewById(R.id.viewpager); // 위의 뷰 페이저
        mViewPager2 = (ViewPager) v.findViewById(R.id.viewpager2); // 위의 뷰 페이저

//        vpPager = (ViewPager) v.findViewById(R.id.vpPager); // 아래 프레그먼트 뷰페이저
//        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
//        vpPager.setAdapter(adapterViewPager);

//        Button btn = (Button) v.findViewById(R.id.giver);
//        Button btn2 = (Button) v.findViewById(R.id.sender);

        //   imageButton = (ImageButton)v.findViewById(R.id.locker_usage);

//        private ImageButton.OnClickListener mButtonClick = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch(v.getId()) {
//                    case R.id.locker_usage:
//                        startActivity(new Intent(getActivity(),PostActivity.class));
//                        break;
//                }
//            }
//        };
        viewPagerSetting();
        viewPagerSetting2();

        TabLayout circleLayout = (TabLayout)v.findViewById(R.id.tab_circle_layout);
        circleLayout.setupWithViewPager(mViewPager, true);;

        TabLayout tabLayout = (TabLayout)v.findViewById(R.id.pager_header);
        tabLayout.setupWithViewPager(mViewPager2, true);


        return v;

    }

    private void viewPagerSetting(){

        mViewPager.setAdapter(new PagerAdapter() {
            String[] titles = {"", "", ""};
            int[] layouts = {com.example.shareinuapp.R.layout.view_index_1, com.example.shareinuapp.R.layout.view_index_2, com.example.shareinuapp.R.layout.view_index_3};


//            private View.OnClickListener mPagerListener1 = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    startActivity(new Intent(getActivity(),PostActivity.class));
//                }
//            };

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                ViewGroup layout = (ViewGroup) inflater.inflate(layouts[position], container, false);
                container.addView(layout);

                View v = null;
                if(position==0){
                    v = inflater.inflate(R.layout.view_index_1, null);
                }
                else if(position == 1){
                    v = inflater.inflate(R.layout.view_index_2, null);
                }
                else{
                    v = inflater.inflate(R.layout.view_index_3, null);
                    //v.findViewById(R.id.locker_usage).setOnClickListener(mButtonClick);
                    v.findViewById(R.id.locker_usage).setOnClickListener(
                            new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getActivity(),LockerExplainActivity.class));
                                }
                            }
                    );
                }



                ((ViewPager)container).addView(v, 0);
                return v;

                //return layout;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                container.removeView((View)object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @Override
            public int getCount() {

//                System.out.println(cnt);
//                    imageButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getActivity(),PostActivity.class));
//
//                        }
//                    });

                return layouts.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        setAutoScroll();
    }
 //밑의 뷰 전환 어댑터
    public static class MyPagerAdapter extends FragmentPagerAdapter {
     private static int NUM_ITEMS = 2;

     public MyPagerAdapter(FragmentManager fragmentManager) {
         super(fragmentManager);
     }

     // Returns total number of pages
     @Override
     public int getCount() {
         return NUM_ITEMS;
     }


     // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
//            switch (position) {
//                case 0: // Fragment # 0 - This will show FirstFragment
//                    return ChildFragment1.newInstance(0, "Page # 1");
//                case 1: // Fragment # 0 - This will show FirstFragment different title
//                    return ChildFragment2.newInstance(1, "Page # 2");
//                default:
                    return null;
//            }
        }

     // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

     }


     private void viewPagerSetting2() {

         mViewPager2.setAdapter(new PagerAdapter() {
             String[] titles = {"나누미","빌리미"};
             int[] layouts = {R.layout.child_fragment1, R.layout.child_fragment2};

             @Override
             public Object instantiateItem(ViewGroup container, int position) {
                 LayoutInflater inflater = LayoutInflater.from(getActivity());
                 ViewGroup layout = (ViewGroup) inflater.inflate(layouts[position], container, false);
                 container.addView(layout);

                 return layout;
             }

             @Override
             public void destroyItem(ViewGroup container, int position, Object object) {
                 container.removeView((View) object);
             }

             @Override
             public CharSequence getPageTitle(int position) {
                 return titles[position];
             }



             @Override
             public int getCount() {
                 return layouts.length;
             }

             @Override
             public boolean isViewFromObject(View view, Object object) {
                 return view == object;
             }
         });

     }

     private void setAutoScroll() {

         final Handler handler = new Handler();
         final Runnable Update = new Runnable() {
             public void run() {
                 if (currentPage == NUM_PAGES) {
                     currentPage = 0;
                 }
                 mViewPager.setCurrentItem(currentPage++, true);
             }
         };


         if(swipeTimer != null)
         {
             swipeTimer.cancel();
         }
         swipeTimer = new Timer();
         swipeTimer.schedule(new TimerTask() {

             @Override
             public void run() {
                 handler.post(Update);
             }
         }, 0, 3000);
     }

     // TODO: Rename method, update argument and hook method into UI event
//     public void onButtonPressed(Uri uri) {
//         if (mListener != null) {
//             // mListener.onFragmentInteraction(uri);
//         }
//     }

//     @Override
//     public void onAttach(Context context) {
//         super.onAttach(context);
//         if (context instanceof OnFragmentInteractionListener) {
//             mListener = (OnFragmentInteractionListener) context;
//         } else {
//             throw new RuntimeException(context.toString()
//                     + " must implement OnFragmentInteractionListener");
//         }
//     }

 //    @Override
//     public void onDetach() {
//         super.onDetach();
//         mListener = null;
//     }


     public interface OnFragmentInteractionListener {
         // TODO: Update argument type and name
         void messageFromParentFragment(Uri uri);
     }
 }
