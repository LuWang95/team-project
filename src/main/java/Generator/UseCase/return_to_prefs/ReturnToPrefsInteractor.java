package Generator.UseCase.return_to_prefs;

public class ReturnToPrefsInteractor implements ReturnToPrefsInputBoundary {
    private final ReturnToPrefsOutputBoundary returnToPrefsOutputBoundary;

    public ReturnToPrefsInteractor(ReturnToPrefsOutputBoundary returnToPrefsOutputBoundary) {
        this.returnToPrefsOutputBoundary = returnToPrefsOutputBoundary;
    }

    @Override
    public void execute(){
        returnToPrefsOutputBoundary.prepareReturnToPrefsSuccessView();
    }
}
