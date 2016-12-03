package com.certoclav.certoscale.model;

import android.widget.TextView;



public class ReferenceField {

private TextView textName;
private TextView textValue;
public ReferenceField(TextView textName, TextView textValue) {
	super();
	this.textName = textName;
	this.textValue = textValue;
}
public TextView getTextName() {
	return textName;
}
public void setTextName(TextView textName) {
	this.textName = textName;
}
public TextView getTextValue() {
	return textValue;
}
public void setTextValue(TextView textValue) {
	this.textValue = textValue;
}




}