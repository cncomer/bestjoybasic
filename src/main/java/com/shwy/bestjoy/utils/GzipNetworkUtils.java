package com.shwy.bestjoy.utils;


import com.shwy.bestjoy.exception.StatusException;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 支持Gzip压缩
 */
public class GzipNetworkUtils {
	private static final String TAG = "GzipNetworkUtils";
	private static boolean DebugMode = true;
	public static void setDebugMode(boolean debugMode) {
		DebugMode = debugMode;
	}
	
	public static HttpResponse openContectionLocked(String uri, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws IOException {
//		DebugUtils.logNetworkOp(TAG, "HttpGet uri=" + uri);
		if (uri == null) {
			return null;
		}
		int index = uri.indexOf("?");
		if (index > 0 && index < uri.length()) {
			if (DebugMode) {
				DebugUtils.logD(TAG, "HttpGet uri=" + uri);
			}
		}
		 
		HttpGet httpRequest = new HttpGet(uri);
		httpRequest.addHeader("Accept-Encoding", "gzip");//设置接受响应消息为gzip

		addSecurityKeyValuesObject(httpRequest, securityKeyValues);
		HttpClient httpClient = /*new DefaultHttpClient();*/AndroidHttpClient.newInstance("android");
		HttpResponse response = httpClient.execute(httpRequest);
		return response;
	}
	
	public static boolean httpStatusOk(int statusCode) {
		return statusCode == HttpStatus.SC_OK || statusCode == 302;
	}
	/**
	 * 
	 * @param uri ��ַ����http://www.baidu.com/
	 * @param path ��Ҫʹ��URLEncoder�����·��
	 * @returnsvn
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse openContectionLocked(String uri, String path, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws ClientProtocolException, IOException {
		String encodedUri = uri + URLEncoder.encode(path);
		return openContectionLocked(encodedUri, securityKeyValues);
	}
	
	public static HttpResponse openContectionLocked(String[] uris, String[] paths, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws ClientProtocolException, IOException {
		StringBuffer encodedUri = new StringBuffer();
		int i = 0;
		for(String uri : uris) {
			encodedUri.append(uri);
			encodedUri.append(URLEncoder.encode(paths[i++]));
		}
		return openContectionLocked(encodedUri.toString(), securityKeyValues);
	}
	
	public static String getUrlEncodedString(String[] params, String[] values) {
		StringBuffer encodedUri = new StringBuffer();
		int i = 0;
		for(String param : params) {
			encodedUri.append(param);
			encodedUri.append(URLEncoder.encode(values[i++]));
		}
		return encodedUri.toString();
	}

	/**
	 * Content-Encoding:"gzip"
	 * @param httpResponse
	 * @return
     */
	public static boolean isGzipSupport(HttpResponse httpResponse) {
		boolean isGzip = false;
		Header[] headers = httpResponse.getHeaders("Content-Encoding");
		for (Header header : headers) {
			if (DebugMode) {
				DebugUtils.logD(TAG, "echo header " + header);
			}
			if (header.getValue().toLowerCase().indexOf("gzip") > -1) {
				isGzip = true;
				break;
			}
		}
		if (DebugMode) {
			DebugUtils.logD(TAG, "isGzipSupport " + isGzip);
		}
		return isGzip;
	}

	/**
	 * Gzip压缩数据
	 * @param source
	 * @return
	 * @throws IOException
     */
	public static byte[] gzip(String source) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzos = new GZIPOutputStream(baos);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(source.getBytes(HTTP.UTF_8));
		try {
			byte[] buffer = new byte[4096];
			int size = byteArrayInputStream.read(buffer);
			while (size >= 0) {
				gzos.write(buffer, 0, size);
				size = byteArrayInputStream.read(buffer);
			}
			gzos.close();
			byte[] result = baos.toByteArray();
			if (DebugMode) {
				DebugUtils.logD(TAG, "gzip result.length= " + result.length + ", source.length= " + source.length());
			}
			return result;
		} finally {
			GzipNetworkUtils.closeInputStream(byteArrayInputStream);
			GzipNetworkUtils.closeOutStream(gzos);
			GzipNetworkUtils.closeOutStream(baos);
		}
	}
	
	/**
	 *
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse openPostContectionLocked(String uri, String paramName, String paramValue, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws ClientProtocolException, IOException {
//		String encodedUri = uri + URLEncoder.encode(path);
//		DebugUtils.logNetworkOp(TAG, "HttpPost uri=" + uri);

		HttpPost httpRequest = new HttpPost(uri);
		httpRequest.addHeader("Accept-Encoding", "gzip");//设置接受响应消息为gzip
		httpRequest.addHeader("Content-Encoding", "gzip");//设置接受响应消息为gzip
		httpRequest.addHeader("Content-Type", "text/plain; charset=UTF-8");
		String contents = paramName + "=" + paramValue;

		if (DebugMode) {
			DebugUtils.logD(TAG, "HttpPost " + uri +  ",contents=" + contents.toString());
		}
		byte[] bgzip = gzip(contents);
        httpRequest.setEntity(new ByteArrayEntity(bgzip));
        addSecurityKeyValuesObject(httpRequest, securityKeyValues);
		HttpClient httpClient = /*new DefaultHttpClient();*/AndroidHttpClient.newInstance("android");
		HttpResponse response = httpClient.execute(httpRequest);
		return response;
	}
	/**
	 * 
	 * @param uri
	 * @param param  参数键值对
	 * @param securityKeyValues
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse openPostContectionLocked(String uri, HashMap<String, String> param, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws ClientProtocolException, IOException {
//		String encodedUri = uri + URLEncoder.encode(path);
//		DebugUtils.logNetworkOp(TAG, "HttpPost uri=" + uri);
		HttpPost httpRequest = new HttpPost(uri);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Set<String>  keySet = param.keySet();
		StringBuilder contents = new StringBuilder();
		for(String key: keySet) {
			contents.append(key).append("=").append(param.get(key));
			contents.append("&");
		}

		contents.deleteCharAt(contents.length()-1);
		if (DebugMode) {
			DebugUtils.logD(TAG, "HttpPost " + uri +  ",contents=" + contents.toString());
		}
//		httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		byte[] bgzip = gzip(contents.toString());
//		String base64 = Base64.encodeToString(bgzip, Base64.DEFAULT);
		httpRequest.setEntity(new ByteArrayEntity(bgzip));
		httpRequest.addHeader("Accept-Encoding", "gzip");//设置接受响应消息为gzip
		httpRequest.addHeader("Content-Encoding", "gzip");//设置接受响应消息为gzip
		httpRequest.addHeader("Content-Type", "text/plain; charset=UTF-8");
		addSecurityKeyValuesObject(httpRequest, securityKeyValues);
		HttpClient httpClient = /*new DefaultHttpClient();*/AndroidHttpClient.newInstance("android");
		HttpResponse response = httpClient.execute(httpRequest);
		return response;
	}
	
	public static HttpResponse openContectionLockedV2(String uri, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws ClientProtocolException, IOException{
//		DebugUtils.logNetworkOp(TAG, "HttpGet uri=" + uri);
		HttpGet httpRequest = new HttpGet(uri);
		httpRequest.addHeader("Accept-Encoding", "gzip");//设置接受响应消息为gzip
		addSecurityKeyValuesObject(httpRequest, securityKeyValues);
		HttpClient httpClient = /*new DefaultHttpClient();*/AndroidHttpClient.newInstance("android");
		return httpClient.execute(httpRequest);
	}
	
	public static String getContentFromHttpResponse(HttpResponse httpResponse)throws Exception{
		InputStream is = null;
		try {
			is = getInputStreamFromHttpResponse(httpResponse);
			String result = getContentFromInput(is);
			if (DebugMode) {
				DebugUtils.logD(TAG, "return getContentFromHttpResponse " + result);
			}
			return result;
		} finally {
			GzipNetworkUtils.closeInputStream(is);
		}
	}

	public static InputStream getInputStreamFromHttpResponse(HttpResponse httpResponse) throws Exception{
		int stateCode = httpResponse.getStatusLine().getStatusCode();
		if (DebugMode) {
			DebugUtils.logD(TAG, "return HttpStatus is " + stateCode);
		}
		if(!httpStatusOk(stateCode)) {
			throw new StatusException(String.valueOf(stateCode), String.valueOf(stateCode));
		}
		// Content-Encoding:"gzip"
		boolean isGzip = isGzipSupport(httpResponse);
		InputStream is = httpResponse.getEntity().getContent();
		if (isGzip) {
			GZIPInputStream gzipIn = new GZIPInputStream(is);
			return gzipIn;
		} else {
			return is;
		}
	}

	/**
	 * 解Gzip压缩
	 *
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static String getContentFromInput(InputStream input) throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[4096];
			int size = input.read(buffer);
			while (size >= 0) {
				out.write(buffer, 0, size);
				size = input.read(buffer);
			}
			out.flush();
			buffer = out.toByteArray();
			out.close();
			String result = new String(buffer, "UTF-8");
			if (DebugMode) {
				DebugUtils.logD(TAG, "getContentFromGzipHttpEntity return " + result);
			}
			return result;
		} finally {
			GzipNetworkUtils.closeInputStream(input);
			GzipNetworkUtils.closeOutStream(out);
		}
	}

	public static void closeInputStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeOutStream(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void addSecurityKeyValuesObject(HttpRequest request, SecurityUtils.SecurityKeyValuesObject keyValues) {
		if (keyValues == null) {
			return;
		}
		HashMap<String, String> map = keyValues.mKeyValuesMap;
		for(String key:map.keySet()) {
			request.addHeader(key, map.get(key));
		}
	}


	/**
	 * 解析对象
	 * @param url
	 * @param securityKeyValues
	 * @return
	 * @throws Exception
     */
	public static ServiceResultObject getServiceResultObjectFromUrl(String url, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws Exception {
		if (DebugMode) {
			DebugUtils.logD(TAG, "getServiceResultObjectFromUrl url=" + url);
		}
		try {
			HttpResponse httpResponse = GzipNetworkUtils.openContectionLocked(url, securityKeyValues);
			ServiceResultObject serviceResultObject = ServiceResultObject.parse(GzipNetworkUtils.getContentFromHttpResponse(httpResponse));
			return serviceResultObject;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 解析对象
	 * @param url
	 * @param securityKeyValues
	 * @return
	 * @throws Exception
	 */
	public static ServiceResultObject postServiceResultObjectFromUrl(String url, HashMap<String, String> param, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws Exception {
		if (DebugMode) {
			DebugUtils.logD(TAG, "postServiceResultObjectFromUrl url=" + url);
		}
		try {
			HttpResponse httpResponse = GzipNetworkUtils.openPostContectionLocked(url, param, securityKeyValues);
			ServiceResultObject serviceResultObject = ServiceResultObject.parse(GzipNetworkUtils.getContentFromHttpResponse(httpResponse));
			return serviceResultObject;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 解析数组对象
	 * @param url
	 * @param securityKeyValues
	 * @return
	 * @throws Exception
	 */
	public static ServiceResultObject getArrayServiceResultObjectFromUrl(String url, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws Exception {
		if (DebugMode) {
			DebugUtils.logD(TAG, "getArrayServiceResultObjectFromUrl url=" + url);
		}
		try {
			HttpResponse httpResponse = GzipNetworkUtils.openContectionLocked(url, securityKeyValues);
			ServiceResultObject serviceResultObject = ServiceResultObject.parseArray(GzipNetworkUtils.getContentFromHttpResponse(httpResponse));
			return serviceResultObject;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static ServiceResultObject postArrayServiceResultObjectFromUrl(String url, HashMap<String, String> param, SecurityUtils.SecurityKeyValuesObject securityKeyValues) throws Exception {
		if (DebugMode) {
			DebugUtils.logD(TAG, "getArrayServiceResultObjectFromUrl url=" + url);
		}
		try {
			HttpResponse httpResponse = GzipNetworkUtils.openPostContectionLocked(url, param, securityKeyValues);
			ServiceResultObject serviceResultObject = ServiceResultObject.parseArray(GzipNetworkUtils.getContentFromHttpResponse(httpResponse));
			return serviceResultObject;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
