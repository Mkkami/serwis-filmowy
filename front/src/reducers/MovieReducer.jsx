import { formActions, formReducer } from "./FormReducer";

export const movieInitialState = {
    values: {
        title: "",
        duration: 0,
        releaseYear: 0,
        categories: new Set()
    },
    errors: {
        title: "",
        duration: "",
        releaseYear: "",
        categories: new Set()
    }
}

export function movieReducer(state, action) {
    if (action.type === formActions.reset) {
        return movieInitialState;
    }
    return formReducer(state, action);
}