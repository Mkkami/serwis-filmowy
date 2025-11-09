import { formActions, formReducer } from "./FormReducer";

export const initialState = {
    values: {
        username: "",
        password: ""
    },
    errors: {
        username: "",
        password: "",
    }
};

export function loginFormReducer(state, action) {
    if (action.type === formActions.reset) {
        return initialState;
    }
    return formReducer(state, action);
}
