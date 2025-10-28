package co.edu.uco.ucochallenge.application;

import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;

public abstract class Response<T> {

    private boolean dataReturned;
    private T data;

    protected Response(final boolean dataReturned, final T data) {
        setDataReturned(dataReturned);
        setData(data);
    }

    private void setDataReturned(final boolean dataReturned) {
        this.dataReturned = dataReturned;
    }

    private void setData(final T data) {
        this.data = ObjectHelper.getDefault(data, null);
    }

    protected boolean isDataReturned() {
        return dataReturned;
    }

    protected T getData() {
        return data;
    }
}
