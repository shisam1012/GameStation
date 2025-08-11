import Select from 'react-select';
import { useState } from 'react';
import { createOnSubmit } from './DifficultyUtils';
import { useLocation, useNavigate } from 'react-router-dom';
import { UserLogin } from '../../context/LoginContext';
import '../../CSS/form.css';
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
            <div className='main-div'>
                <form onSubmit={onSubmit}>
                    <label>Choose difficulty:</label>
                    <Select
                        options={options}
                        value={difficulty}
                        onChange={handleChange}
                    />
                    <div>
                        <input
                            type='submit'
                            className='button-form'
                            value='Submit'
                        />
                    </div>
                </form>
                <div className='text-info'>
                    <p>
                        For the Easy difficulty you will get 1 point for a win,
                    </p>
                    <p>For Medium - 3 points,</p>
                    <p>And for Hard - 5 points.</p>
                    <p>
                        if you disconnect during the game - it will count as a
                        loss and the opponent will get the points!
                    </p>
                </div>
            </div>
        </div>
    );
}

export default Connect4Difficulty;
