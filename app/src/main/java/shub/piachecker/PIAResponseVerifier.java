package shub.piachecker;

/**
 * Created by Ceasar on 08/09/2016.
 */
public class PIAResponseVerifier {

    public Boolean isPIAVerified(String htmlContent){
        if(htmlContent == null)
            return false;

        if(htmlContent.contains("You are protected by PIA")) // TODO: more robust check
            return true;

        return false;
    }

}
