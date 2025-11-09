import { useReducer } from "react"
import { loginFormReducer, initialState } from "../reducers/LoginFormReducer"
import { formActions } from "../reducers/FormReducer"

function AuthForm({onValidate, onSubmit, action}) {
    const [state, dispatch] = useReducer(loginFormReducer, initialState )

    const handleChange = (e) => {
        dispatch({
            type: formActions.changeField,
            field: e.target.name,
            value: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const validationErrors = onValidate(state.values);
        if (Object.keys(validationErrors).length > 0) {
            Object.entries(validationErrors).forEach(([field, error]) => {
                dispatch({type: formActions.setError, field, error})
            });
            return;
        };
        onSubmit(state.values);
        dispatch({type: formActions.reset});
    }

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label htmlFor="username">Username:</label>
                <input type="text" name="username" value={state.values["username"]} onChange={handleChange} />
                {state.errors["username"] && <p className="error">{state.errors["username"]}</p>}
            </div>
            <div>
                <label htmlFor="password">Password:</label>
                <input type="password" name="password" value={state.values["password"]} onChange={handleChange} />
                {state.errors["password"] && <p className="error">{state.errors["password"]}</p>}
            </div>
            <button type="submit">{action}</button>
        </form>
    )
}
export default AuthForm;