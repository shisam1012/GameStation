import Select from 'react-select';
import { useState } from 'react';
import { createOnSubmit } from './DifficultyUtils';
import { useNavigate } from 'react-router-dom';
import { UserLogin } from '../context/LoginContext';

function Connect4Difficulty() {
    const options = [
        { value: 'Easy', label: 'Easy' },
        { value: 'Medium', label: 'Medium' },
        { value: 'Hard', label: 'Hard' },
    ];
    const { userLoggedIn } = UserLogin();
    const navigate = useNavigate();
    const [difficulty, setDifficulty] = useState(options[0]);
    const onSubmit = createOnSubmit(
        difficulty.value,
        navigate,
        userLoggedIn.username
    );
    const handleChange = (selectedOption) => {
        setDifficulty(selectedOption);
    };

    return (
        <div>
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
