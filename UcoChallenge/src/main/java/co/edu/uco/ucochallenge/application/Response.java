package co.edu.uco.ucochallenge.application;

public abstract class Response <T> {
	
	private boolean dataReturned;
	private T data;
	
	protected Response(final boolean dataReturned,final T data) {
		setDataReturned(dataReturned);
		setData(data);
	}

	private void setDataReturned(boolean dataReturned) {
		//Limpieza de datos
		this.dataReturned = dataReturned;
	}

	private void setData(T data) {
		//Limpieza de datos - Con los helpers
		this.data = data;
	}

	protected boolean isDataReturned() {
		return dataReturned;
	}

	protected T getData() {
		return data;
	}

}
