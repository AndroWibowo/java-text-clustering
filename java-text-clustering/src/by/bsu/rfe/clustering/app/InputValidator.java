package by.bsu.rfe.clustering.app;

public interface InputValidator {

  public boolean validate(String value);

  public String getErrorMessage();

}
