package com.baoxw.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;



public class MainActivity extends Activity implements 
      OnClickListener, TextWatcher, OnPoiSearchListener ,InputtipsListener{
	
	private AMap mAMap;
	private MapView mapView;
	private AutoCompleteTextView searchText;// 输入搜索关键字
	private String keyWord = "";// 要输入的poi搜索关键字
	private Button searButton; //搜索按钮
	private PoiResult poiResult; // poi返回的结果
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mapView = (MapView) this.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
			
		if (mAMap == null) {
			mAMap = mapView.getMap();
			setUpMap();
		}
	}

	
	private void setUpMap() {
//		搜索按钮
		searButton = (Button) findViewById(R.id.mButton);
		searButton.setOnClickListener(this);
				
		searchText = (AutoCompleteTextView) findViewById(R.id.mAutoCompleteTextView);
		searchText.addTextChangedListener(this);// 添加文本输入框监听事件
				
	}

     public void searchButton() {
		
		keyWord = searchText.getText().toString().trim();
		
		if ("".equals(keyWord)) {
			Toast.makeText(MainActivity.this, "请输入搜索关键字", Toast.LENGTH_LONG).show();
			return;
		} else {
			doSearchQuery();
		}
	 }
	
     protected void doSearchQuery() {		
 		currentPage = 0;
 		query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
 		query.setPageSize(10);// 设置每页最多返回多少条poiitem
 		query.setPageNum(currentPage);// 设置查第一页

 		poiSearch = new PoiSearch(this, query);
 		poiSearch.setOnPoiSearchListener(this);
 		poiSearch.searchPOIAsyn();
 	}
     
	
	@Override
	public void onClick(View arg0) {
		searchButton();		
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
		String newText = arg0.toString().trim();
		InputtipsQuery inputquery = new InputtipsQuery(newText, "");
		Inputtips inputTips = new Inputtips(MainActivity.this, inputquery);
		inputTips.setInputtipsListener(this);
		inputTips.requestInputtipsAsyn();
		
	}

	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

					if (poiItems != null && poiItems.size() > 0) {
						mAMap.clear();// 清理之前的图标
						PoiOverlay poiOverlay = new PoiOverlay(mAMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
						
					} else {
						Toast.makeText(MainActivity.this, "对不起，没有搜到相关数据", Toast.LENGTH_LONG).show();
					}
				}
			} else {
				Toast.makeText(MainActivity.this, "对不起，没有搜到相关数据", Toast.LENGTH_LONG).show();
			}
			
		} else if (rCode == 27) {
			Toast.makeText(MainActivity.this, "搜索失败，请链接网络", Toast.LENGTH_LONG).show();
		} else if (rCode == 32) {
			Toast.makeText(MainActivity.this, "key验证无效", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(MainActivity.this, "未知错误，请稍后重试，错误码为"+rCode, Toast.LENGTH_LONG).show();
		}

		
	}
	
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		
		Toast.makeText(MainActivity.this, infomation, Toast.LENGTH_LONG).show();

	}

	@Override
	protected void onResume() {
		
		super.onResume();
		mapView.onResume();
	}
	@Override
	protected void onPause() {
		
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		mapView.onDestroy();
	}


	@Override
	public void onGetInputtips(List<Tip> tipList, int rCode) {
		if (rCode == 0) {// 正确返回
			List<String> listString = new ArrayList<String>();
			for (int i = 0; i < tipList.size(); i++) {
				listString.add(tipList.get(i).getName());
			}
			ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
					getApplicationContext(),
					R.layout.route_inputs, listString);
			searchText.setAdapter(aAdapter);
			aAdapter.notifyDataSetChanged();
		}
	}
}
