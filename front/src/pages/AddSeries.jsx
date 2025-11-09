import { useReducer, useState } from "react";
import { formActions } from "../reducers/FormReducer";
import Categories from "../components/Categories";
import { seriesInitialState, seriesReducer } from "../reducers/SeriesReducer";

function AddSeries() {
    const [state, dispatch] = useReducer(seriesReducer, seriesInitialState);

    const handleChange = (e) => {
        dispatch({
            type: formActions.changeField,
            field: e.target.name,
            value: e.target.value
        })
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        alert("submitted");
    }

    return (
        <div className="movie-form form">
            <h2>Add new series</h2>
            <form>
                <div className="form-container">
                    <div className="input-container">
                        <label htmlFor="title">Title</label>
                        <input placeholder="title" name="title" onChange={handleChange} type="text"></input>
                    </div>
                    <div className="input-container">
                        <label htmlFor="releaseYear">Release Year</label>
                        <input placeholder="year" name="releaseYear" onChange={handleChange} type="number"></input>
                    </div>
                    <div className="input-container">
                        <label htmlFor="endYear">End Year</label>
                        <input placeholder="year" name="endYear" onChange={handleChange} type="number"></input>
                    </div>
                </div>
                <div>
                    <h3>Categories</h3>
                    <Categories/>
                </div>
                
            </form>
        </div>
    )
}
export default AddSeries;