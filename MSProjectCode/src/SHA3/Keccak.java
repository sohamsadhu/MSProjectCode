package SHA3;

public interface Keccak 
{
  public boolean setWidth( int length );
  
  public int getWidth();
  
  public void pad();
  
  public void sponge();
  
  public void theta();
  
  public void rho();
  
  public void pi();
  
  public void chi();
  
  public void iota();
}
