import { formActions, formReducer } from "./FormReducer";

export const seriesInitialState = {
    values: {
        title: "",
        releaseYear: 0,
        endYear: 0,
        categories: []
    },
    errors: {
        title: "",
        duration: "",
        releaseYear: "",
        categories: []
    }
}

export function movieReducer(state, action) {
    if (action.type === formActions.reset) {
        return seriesInitialState;
    }
    return formReducer(state, action);
}