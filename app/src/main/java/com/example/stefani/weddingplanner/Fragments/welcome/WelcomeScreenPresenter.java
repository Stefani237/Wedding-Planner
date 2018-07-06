package com.example.stefani.weddingplanner.Fragments.welcome;


public class WelcomeScreenPresenter implements IWelcomeScreen.Presenter {


    private IWelcomeScreen.View mView;


    public WelcomeScreenPresenter(IWelcomeScreen.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
    }


    @Override
    public void initializeViews() {

    }
}
