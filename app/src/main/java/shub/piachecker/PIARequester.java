package shub.piachecker;

/**
 * Created by Ceasar on 08/09/2016.
 */
public class PIARequester {

    final Result<Boolean> _vpnStatus;
    final IEventHandler _onCheckComplete;
    final String _vpnCheckEndpoint = "https://www.privateinternetaccess.com";
    final Result<String> _response = new Result<String>();
    final PIAResponseVerifier _verifier = new PIAResponseVerifier();
    final IEventHandler _onRequestComplete = new EventHandler(new Runnable() {
        public void run() { processResult(); }
    });

    public PIARequester(final Result<Boolean> vpnStatus, final IEventHandler onCheckComplete){
        _vpnStatus = vpnStatus;
        _onCheckComplete = onCheckComplete;
    }

    public void getPIAStatusAsync() throws Exception {
        new WebRequester().getContentAsync(
                _vpnCheckEndpoint,
                _response,
                _onRequestComplete);
    }

    private void processResult() {
        if(_response.exception != null)
            _vpnStatus.exception = _response.exception;

        _vpnStatus.result = _verifier.isPIAVerified(_response.result);
        _onCheckComplete.onEvent();
    }
}
