import React from "react";
import translations from "./DisplayGroupsTable.element.translations";
import {
    DigitTable,
    useDigitTranslations
} from "@cthit/react-digit-components";
import {
    DESCRIPTION,
    EMAIL,
    FUNCTION,
    ID,
    NAME,
    PRETTY_NAME
} from "../../../api/groups/props.groups.api";

function generateHeaderTexts(text) {
    const output = {};

    output[ID] = text.Id;
    output[NAME] = text.Name;
    output[DESCRIPTION] = text.Description;
    output[EMAIL] = text.Email;
    output[FUNCTION] = text.Function;
    output[PRETTY_NAME] = text.PrettyName;
    output["__link"] = text.Details;

    return output;
}

function modifyData(groups, text, activeLanguage, columns) {
    return groups.map(group => {
        const newGroup = { ...group };

        newGroup[ID] = group[ID];
        newGroup[NAME] = group[NAME];
        newGroup[DESCRIPTION] =
            columns.includes(DESCRIPTION) && group[DESCRIPTION] != null
                ? group[DESCRIPTION][activeLanguage]
                : null;
        newGroup[EMAIL] = group[EMAIL];
        newGroup[FUNCTION] =
            columns.includes(FUNCTION) && group[FUNCTION] != null
                ? group[FUNCTION][activeLanguage]
                : null;
        newGroup["__link"] = "/groups/" + group[ID];

        return newGroup;
    });
}

const DisplayGroupsTable = ({ title, groups, columnsOrder, margin }) => {
    const [text, activeLanguage] = useDigitTranslations(translations);

    if (groups == null) {
        return null;
    }

    return (
        <DigitTable
            margin={margin != null ? margin : "0px"}
            titleText={title ? title : text.Groups}
            searchText={text.SearchForGroups}
            idProp="id"
            startOrderBy={NAME}
            columnsOrder={columnsOrder}
            headerTexts={generateHeaderTexts(text)}
            data={modifyData(groups, text, activeLanguage, columnsOrder)}
            emptyTableText={text.NoGroups}
        />
    );
};

DisplayGroupsTable.defaultProps = {
    columnsOrder: [NAME, EMAIL]
};

export default DisplayGroupsTable;
