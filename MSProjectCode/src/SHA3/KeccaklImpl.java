package SHA3;

public class KeccaklImpl implements Keccak 
{
  private int width;  
  private byte[][][] state;
  
  public boolean setWidth( int length )
  {
    if ((length > -1) && (length < 7))
    {
      width = (int)Math.pow(2, length);
      state = new byte[5][5][width];
      return true;
    }
    else {
      return false;
    }
  }
  
  public int getWidth()
  { 
    return this.width;
  }
  
  public void pad(){}
  
  public void sponge(){}
  
  public void theta(){}
  
  public void rho(){}
  
  public void pi(){}
  
  public void chi(){}
  
  public void iota(){}
  
}
