import React, { Component } from "react";

import { MarginTop, Center } from "../../../../common-ui/layout";
import {
  GammaCard,
  GammaCardTitle,
  GammaCardBody,
  GammaCardButtons,
  GammaCardSubTitle
} from "../../../../common-ui/design";

import { CIDInput } from "./styles";
import { GammaButton } from "../../../../common/gui/gamma-button";
import { GammaStepper } from "../../../../common/gui/gamma-stepper";
import { GammaSwitch } from "../../../../common/gui/gamma-switch";

class InputCidScreen extends Component {
  state = {
    cid: ""
  };

  constructor() {
    super();

    this.cidInputRef = React.createRef();
  }

  render() {
    return (
      <MarginTop>
        <Center>
          <GammaCard absWidth="300px" absHeight="300px" hasSubTitle>
            <GammaCardTitle>Skriv in ditt CID</GammaCardTitle>
            <GammaCardSubTitle>
              Vi kommer skicka ett mail till din studentmail för att bekräfta
              din identitet.
            </GammaCardSubTitle>
            <GammaCardBody>
              <Center>
                <CIDInput
                  innerRef={this.cidInputRef}
                  validate={value => {
                    return value.length > 0;
                  }}
                  maxLength={10}
                  lowerLabelReflectLength
                  onChange={value =>
                    this.setState({
                      ...this.state,
                      cid: value
                    })
                  }
                  upperLabel="CID"
                />
              </Center>
            </GammaCardBody>
            <GammaCardButtons reverseDirection>
              <GammaButton
                text="Skicka CID"
                onClick={() => {
                  if (this.state.cid.length == 0) {
                    this.cidInputRef.current.invalidate();
                    this.props.showError(
                      "Du har inte skrivit in något cid ännu"
                    );
                  } else {
                    this.props.sendCid(this.state.cid);
                  }
                }}
                primary
                raised
              />
            </GammaCardButtons>
          </GammaCard>
        </Center>
      </MarginTop>
    );
  }
}

export default InputCidScreen;
