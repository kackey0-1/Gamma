import React, { useMemo, useState } from "react";
import {
    DigitButton,
    DigitSelectMultipleTable,
    DigitText,
    DigitLayout,
    DigitDesign,
    useDigitTranslations
} from "@cthit/react-digit-components";
import translations from "./SelectMembers.view.translations";
import UsersInGroupChanges from "./elements/users-in-group-changes";
import * as _ from "lodash";
import { GROUP_PRETTY_NAME } from "../../../../api/groups/props.groups.api";
import {
    USER_FIRST_NAME,
    USER_ID,
    USER_LAST_NAME,
    USER_NICK
} from "../../../../api/users/props.users.api";
import styled from "styled-components";

const CustomRow = styled.div`
    display: flex;
    flex-direction: row;

    @media (max-width: 600px) {
        flex-direction: column;
    }
`;

const generateHeaderTexts = text => {
    const headerTexts = {};

    headerTexts[USER_FIRST_NAME] = text.FirstName;
    headerTexts[USER_LAST_NAME] = text.LastName;
    headerTexts[USER_NICK] = text.Nickname;
    headerTexts[USER_ID] = text.Id;

    return headerTexts;
};

const SelectMembers = ({ users, group, onMembersSelected }) => {
    const [text] = useDigitTranslations(translations);
    const [selectedMemberIds, setSelectedMemberIds] = useState(
        group.groupMembers.map(member => member.id)
    );

    const unsavedEdits = useMemo(
        () => selectedMemberIds.length !== group.groupMembers.length,
        [selectedMemberIds, group.groupMembers]
    );

    return (
        <DigitLayout.Column>
            <DigitDesign.Card margin={{ bottom: "8px" }}>
                <DigitDesign.CardBody>
                    <DigitLayout.Row
                        justifyContent={"space-between"}
                        alignItems={"center"}
                    >
                        <DigitText.Heading5
                            text={
                                unsavedEdits
                                    ? text.UnsavedEdits
                                    : text.NoChanges
                            }
                        />
                        <DigitButton
                            raised
                            primary
                            text={text.Next}
                            onClick={() => {
                                onMembersSelected(selectedMemberIds);
                            }}
                        />
                    </DigitLayout.Row>
                </DigitDesign.CardBody>
            </DigitDesign.Card>
            <CustomRow>
                <UsersInGroupChanges
                    currentMembers={group.groupMembers}
                    selectedMembers={selectedMemberIds.map(memberId =>
                        _.find(users, { id: memberId })
                    )}
                />
                <DigitSelectMultipleTable
                    margin={"4px"}
                    disableSelectAll
                    flex={"2"}
                    value={selectedMemberIds}
                    onChange={newSelected => {
                        setSelectedMemberIds(newSelected);
                    }}
                    search
                    titleText={text.UsersFor + group[GROUP_PRETTY_NAME]}
                    searchText={text.Search}
                    idProp="id"
                    startOrderBy={USER_NICK}
                    columnsOrder={[USER_FIRST_NAME, USER_NICK, USER_LAST_NAME]}
                    headerTexts={generateHeaderTexts(text)}
                    data={users}
                />
            </CustomRow>
        </DigitLayout.Column>
    );
};

export default SelectMembers;
