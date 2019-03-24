import React from "react";
import {
    DigitFAB,
    DigitTable,
    DigitTranslations
} from "@cthit/react-digit-components";

import translations from "./DisplayUsersTable.element.translations";

import {
    ACCEPTANCE_YEAR,
    CID,
    FIRST_NAME,
    LAST_NAME,
    NICK
} from "../../../api/users/props.users.api";

function generateHeaderTexts(text) {
    const headerTexts = {};

    headerTexts[FIRST_NAME] = text.FirstName;
    headerTexts[LAST_NAME] = text.LastName;
    headerTexts[CID] = text.Cid;
    headerTexts[NICK] = text.Nick;
    headerTexts[ACCEPTANCE_YEAR] = text.AcceptanceYear;
    headerTexts["__link"] = text.Details;

    return headerTexts;
}

const DisplayUsersTable = ({ users }) => (
    <DigitTranslations
        translations={translations}
        render={text => (
            <DigitTable
                titleText={text.Users}
                searchText={text.SearchForUsers}
                idProp={CID}
                startOrderBy={FIRST_NAME}
                columnsOrder={[
                    FIRST_NAME,
                    NICK,
                    LAST_NAME,
                    CID,
                    ACCEPTANCE_YEAR
                ]}
                headerTexts={generateHeaderTexts(text)}
                data={users.map(user => {
                    return {
                        ...user,
                        __link: "/users/" + user.id
                    };
                })}
                emptyTableText={text.NoUsers}
            />
        )}
    />
);

export default DisplayUsersTable;
