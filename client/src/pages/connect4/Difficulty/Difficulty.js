import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Select from 'react-select';

import BackButton from '../../../components/BackButton/BackButton';
import Bar from '../../../components/Bar/Bar';
import { UserLogin } from '../../../context/LoginContext';
import { createOnSubmit } from './DifficultyService';

import '../../../styles/Form.css';

/**
 *
 * @returns the difficulty selection page - includes the bar component, back button, select with 3 options, submit button and some information about the score
 */
function Connect4Difficulty() {
    //there are 3 options for difficulties
    const options = [
        { value: 'Easy', label: 'Easy' },
        { value: 'Medium', label: 'Medium' },
        { value: 'Hard', label: 'Hard' },
    ];
    const { userLoggedIn } = UserLogin();
    const navigate = useNavigate();
    const location = useLocation();
    const [difficulty, setDifficulty] = useState(options[0]);

    //if the user is not logged in - can not choose a difficulty
    if (!userLoggedIn) {
        return <p>Please login to see choose a difficulty.</p>;
    }

    const onSubmit = createOnSubmit(
        //function from DifficultyService.js
        difficulty.value,
        navigate,
        userLoggedIn.username,
        location
    );

    const handleChange = (selectedOption) => {
        setDifficulty(selectedOption);
    };

    //returns the difficulty selection page with all the components and info needed
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
