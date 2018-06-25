import axios from "axios";
import { redirectTo } from "../../app/views/gamma-redirect/GammaRedirect.view.action-creator";
//import { toastOpen } from "../../app/views/gamma-toast/GammaToast.view.action-creator";

import {
  CREATE_ACCOUNT_VALIDATING_CID,
  CREATE_ACCOUNT_VALIDATE_CID_FAILED,
  CREATE_ACCOUNT_VALIDATE_CID_SUCCESSFULLY,
  CREATE_ACCOUNT_VALIDATING_CODE_AND_DATA,
  CREATE_ACCOUNT_VALIDATE_CODE_FAILED,
  CREATE_ACCOUNT_VALIDATE_DATA_FAILED,
  CREATE_ACCOUNT_VALIDATE_CODE_AND_DATA_SUCCESSFULLY,
  CREATE_ACCOUNT_COMPLETED
} from "./CreateAccount.actions";

export function createAccountValidateCid(cid) {
  return dispatch => {
    dispatch(createAccountValidatingCid());
    axios
      .post(
        "http://localhost:8081/whitelist/valid",
        {
          cid: cid
        },
        {
          "Content-Type": "application/json"
        }
      )
      .then(response => {
        console.log(response);
        //dispatch(createAccountValidateCidSuccessfully());
        dispatch(redirectTo("/create-account/email-sent"));
      })
      .catch(error => {
        //Temp
        dispatch(redirectTo("/create-account/email-sent"));
        /*dispatch(
          createAccountValidateCidFailed(
            "Något gick fel när cid försöktes valideras"
          )
        );*/
        /*dispatch(
          toastOpen({
            text: "Something went wrong"
          })
        );*/
        console.log(error);
      });
  };
}

export function createAccountValidatingCid() {
  return {
    type: CREATE_ACCOUNT_VALIDATING_CID,
    error: false
  };
}

export function createAccountValidateCidFailed(error) {
  return {
    type: CREATE_ACCOUNT_VALIDATE_CID_FAILED,
    error: true,
    payload: {
      error: error
    }
  };
}

export function createAccountValidateCidSuccessfully() {
  return {
    type: CREATE_ACCOUNT_VALIDATE_CID_SUCCESSFULLY,
    error: false
  };
}

//data includes code. Ignore code for now
export function createAccountValidateCodeAndData(data) {
  return dispatch => {
    dispatch(createAccountValidatingCodeAndData());
    axios
      .post("http://localhost:8081/users/create", data, {
        "Content-Type": "application/json"
      })
      .then(response => {
        dispatch(createAccountValidateCodeAndDataSuccessfully());
        dispatch(redirectTo("/create-account/finished"));
      })
      .catch(error => {
        dispatch(createAccountValidateDataFailed(error));
      });
  };
}

export function createAccountValidatingCodeAndData() {
  return {
    type: CREATE_ACCOUNT_VALIDATING_CODE_AND_DATA,
    error: false
  };
}

export function createAccountValidateCodeFailed(error) {
  return {
    type: CREATE_ACCOUNT_VALIDATE_CODE_FAILED,
    error: true,
    payload: {
      error: error
    }
  };
}

export function createAccountValidateDataFailed(error) {
  return {
    type: CREATE_ACCOUNT_VALIDATE_DATA_FAILED,
    error: true,
    payload: {
      error: error
    }
  };
}

export function createAccountValidateCodeAndDataSuccessfully() {
  return {
    type: CREATE_ACCOUNT_VALIDATE_CODE_AND_DATA_SUCCESSFULLY,
    error: false
  };
}

export function createAccountCompleted() {
  return {
    type: CREATE_ACCOUNT_COMPLETED,
    error: false
  };
}
