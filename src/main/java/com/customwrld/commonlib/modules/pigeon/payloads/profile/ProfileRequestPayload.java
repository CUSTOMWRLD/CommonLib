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

/**
 * Used for requesting a new profile or replacing the current one with a newer/more up-to-date one.
 */
@Getter @Payload
public class ProfileRequestPayload extends FeedbackPayload {

    public ProfileRequestPayload() {
        this.payloadId = "profileRequestPayload";
        this.payloadTimeout = 10000;
    }

    //Request

    @Nullable @Transmit(direction = RequiredState.REQUEST) String name;
    @Nullable @Transmit(direction = RequiredState.REQUEST) UUID id;

    @RequestConstructor
    public ProfileRequestPayload(String name, UUID id, Consumer<ProfileRequestPayload> feedback) {
        this();
        this.payloadState = FeedbackState.REQUEST;
        this.feedbackID = UUID.randomUUID();

        this.name = name;
        this.id = id;

        this.feedback = feedback;
    }

    @RequestConstructor
    public ProfileRequestPayload(String name, Consumer<ProfileRequestPayload> feedback) {
        this(name, null, feedback);
    }

    @RequestConstructor
    public ProfileRequestPayload(UUID id, Consumer<ProfileRequestPayload> feedback) {
        this(null, id, feedback);
    }

    //Response

    @Nullable @Transmit(direction = RequiredState.RESPONSE) ProfileQueryResult result;
    @Nullable @Transmit(direction = RequiredState.RESPONSE) Profile profile;

    @ResponseConstructor
    public ProfileRequestPayload(ProfileQueryResult result, Profile profile) {
        this();
        this.payloadState = FeedbackState.RESPONSE;

        this.result = result;
        this.profile = profile;
    }

    @Override
    public void receive() {}

}