package com.customwrld.commonlib.modules.pigeon.payloads.profile;


import com.customwrld.commonlib.modules.profile.Profile;
import com.customwrld.commonlib.modules.profile.ProfileQueryResult;
import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.RequestConstructor;
import com.customwrld.pigeon.annotations.ResponseConstructor;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.feedback.FeedbackState;
import com.customwrld.pigeon.feedback.RequiredState;
import com.customwrld.pigeon.payloads.bases.FeedbackPayload;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

@Getter @Payload
public class ProfileCreatePayload extends FeedbackPayload {

    public ProfileCreatePayload() {
        this.payloadId = "profileCreatePayload";
        this.payloadTimeout = 10000;
    }

    //Request

    @Transmit(direction = RequiredState.REQUEST) Profile profile;

    @RequestConstructor
    public ProfileCreatePayload(Profile profile, Consumer<ProfileCreatePayload> feedback) {
        this();
        this.payloadState = FeedbackState.REQUEST;
        this.feedbackID = UUID.randomUUID();

        this.profile = profile;

        this.feedback = feedback;
    }

    //Response

    @Nullable @Transmit(direction = RequiredState.RESPONSE) ProfileQueryResult result;

    @ResponseConstructor
    public ProfileCreatePayload(ProfileQueryResult result) {
        this();
        this.payloadState = FeedbackState.RESPONSE;

        this.result = result;
    }

    @Override
    public void receive() {}

}