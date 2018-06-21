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

class InputCidScreen extends Component {
  state = {
    cid: ""
  };

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
                  startValue="hej"
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
                  this.props.sendCid(this.state.cid);
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
