import { useReducer, useState } from "react";
import { movieInitialState, movieReducer } from "../reducers/MovieReducer";
import { formActions } from "../reducers/FormReducer";
import Categories from "../components/Categories";
import { addNewFilm } from "../api/api";

function MovieForm() {
    const [state, dispatch] = useReducer(movieReducer, movieInitialState);
    const [message, setMessage] = useState("");

    const handleChange = (e) => {
        dispatch({
            type: formActions.changeField,
            field: e.target.name,
            value: e.target.value
        })
    }

    const handleCategoryUpdate = (newCategoriesSet) => {
        dispatch({
            type: formActions.changeField,
            field: 'categories',
            value: newCategoriesSet
        });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm(state.values)) {
            return;
        }
        const res = await addNewFilm(
            state.values.title,
            state.values.duration,
            state.values.releaseYear,
            Array.from(state.values.categories).map(c => c.name));

        if (res.ok) {
            setMessage("Movie added");
            return;
        }
        if (!res.ok) {
            setMessage("Error");
            return;
        }
    }

    const validateForm = (values) => {
        const errors = {}
        if (!values.title) errors.title = "Title missing";
        if (!values.duration || values.duration <= 0) errors.duration = "Duration must be longer than 0 minutes";
        if (!values.releaseYear || values.releaseYear <= 0) errors.releaseYear = "Release year missing";
        Object.keys(values).forEach(field => {
            if (!errors[field]) {
                dispatch({
                    type: formActions.setError,
                    field: field,
                    error: "",
                })
            } else {
                dispatch({
                    type: formActions.setError,
                    field: field,
                    error: errors[field],
                })
            }
        })
        return Object.keys(errors).length === 0;
    }

    return (
        <div className="movie-form form">
            <h2>Add new movie</h2>
            <form onSubmit={handleSubmit}>
                <div className="main-content">
                    <div className="form-container">
                        <div className="input-container">
                            <label htmlFor="title">Title</label>
                            <input placeholder="title" name="title" onChange={handleChange} type="text"></input>
                            {state.errors["title"] && <p className="error">{state.errors["title"]}</p>}
                        </div>
                        <div className="input-container">
                            <label htmlFor="duration">Duration</label>
                            <input placeholder="minutes" name="duration" onChange={handleChange} type="number"></input>
                            {state.errors["duration"] && <p className="error">{state.errors["duration"]}</p>}
                        </div>
                        <div className="input-container">
                            <label htmlFor="releaseYear">Release Year</label>
                            <input placeholder="year" name="releaseYear" onChange={handleChange} type="number"></input>
                            {state.errors["releaseYear"] && <p className="error">{state.errors["releaseYear"]}</p>}
                        </div>
                    </div>
                    <div className="categories-wrapper">
                        <h3>Categories</h3>
                        <Categories heightLimit={true} selectedCategories={state.values.categories} setSelectedCategories={handleCategoryUpdate}/>
                    </div>
                </div>
                {message && <p>{message}</p>}
                <button type="submit">Submit</button>
            </form>
        </div>
    )
}
export default MovieForm;