import Select from 'react-select';
import { useState } from 'react';
import { createOnSubmit } from './DifficultyUtils';
import { useLocation, useNavigate } from 'react-router-dom';
import { UserLogin } from '../../context/LoginContext';

import BackButton from '../BackButton/BackButton';
import Bar from '../Bar';
function Connect4Difficulty() {
    const options = [
        { value: 'Easy', label: 'Easy' },
        { value: 'Medium', label: 'Medium' },
        { value: 'Hard', label: 'Hard' },
    ];
    const { userLoggedIn } = UserLogin();
    const navigate = useNavigate();
    const location = useLocation();
    const [difficulty, setDifficulty] = useState(options[0]);
    const onSubmit = createOnSubmit(
        difficulty.value,
        navigate,
        userLoggedIn.username,
        location
    );
    const handleChange = (selectedOption) => {
        setDifficulty(selectedOption);
    };

    return (
        <div>
            <Bar />
            <BackButton />
            <form onSubmit={onSubmit}>
                <label>Choose difficulty:</label>
                <Select
                    options={options}
                    value={difficulty}
                    onChange={handleChange}
                />
                <input type='submit' value='Submit' />
            </form>
        </div>
    );
}

export default Connect4Difficulty;
