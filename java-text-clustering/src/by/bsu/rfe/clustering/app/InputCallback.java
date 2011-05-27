package by.bsu.rfe.clustering.app;

public interface InputCallback {

  public void inputValid(String input);

  public void inputInvalid(String input);
  
  public void inputCancelled();

}
