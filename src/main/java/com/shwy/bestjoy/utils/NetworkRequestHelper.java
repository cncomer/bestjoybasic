package com.shwy.bestjoy.utils;

/**
 * Created by bestjoy on 16/4/6.
 */
public class NetworkRequestHelper {

    public interface IRequestRespond {
        public void onRequestEnd(Object result);
        public void onRequestStart();
        public void onRequestCancelled();
        public Object doInBackground();
    }

    public static RequestAsyncTask requestAsync(IRequestRespond requestRespond) {
        RequestAsyncTask requestAsyncTask = new RequestAsyncTask(requestRespond);
        requestAsyncTask.execute();
        return requestAsyncTask;
    }

    public static class RequestAsyncTask extends AsyncTaskCompat<Void, Object, Object> {
        private IRequestRespond _requestRespond;
        private RequestAsyncTask(IRequestRespond requestRespond) {
            _requestRespond = requestRespond;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _requestRespond.onRequestStart();
        }

        @Override
        protected Object doInBackground(Void... params) {
           return _requestRespond.doInBackground();
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            _requestRespond.onRequestEnd(result);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            _requestRespond.onRequestCancelled();
        }

    }
}
