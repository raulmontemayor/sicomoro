package org.iemm.sicomoro.controller.jmesa;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.jmesa.util.ItemUtils;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;

public class ImageCellEditor implements CellEditor {

	private String image;
	private String jsFunction;
	private String[] params;

	public ImageCellEditor(String image, String jsFunction, String... params) {
		Validate.notEmpty(image);
		Validate.notEmpty(jsFunction);
		this.image = image;
		this.jsFunction = jsFunction;
		this.params = params;
	}

	@Override
	public Object getValue(Object item, String property, int rowCount) {
		final Object value = ItemUtils.getItemValue(item, property);
		final HtmlBuilder html = new HtmlBuilder();

		final StringBuilder sb = new StringBuilder();
		sb.append(jsFunction).append("('").append(value).append('\'');
		if (ArrayUtils.isNotEmpty(params)) {
			for (int i = 0; i < params.length; i++) {
				final Object param = ItemUtils.getItemValue(item, params[i]);
				sb.append('\'');
				sb.append(param == null ? "" : param.toString());
				sb.append('\'');
				if (i != params.length - 1) {
					sb.append(',');
				}
			}
		}
		sb.append(");");

		html.img().src(image).onclick(sb.toString()).style("cursor:pointer;").close();
		return html.toString();
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the jsFunction
	 */
	public String getJsFunction() {
		return jsFunction;
	}

	/**
	 * @param jsFunction
	 *            the jsFunction to set
	 */
	public void setJsFunction(String jsFunction) {
		this.jsFunction = jsFunction;
	}

	/**
	 * @return the params
	 */
	public String[] getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(String[] params) {
		this.params = params;
	}
}
