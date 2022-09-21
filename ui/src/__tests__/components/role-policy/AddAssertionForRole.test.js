/*
 * Copyright 2020 Verizon Media
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import { fireEvent, render } from '@testing-library/react';
import AddAssertionForRole from '../../../components/role-policy/AddAssertionForRole';
import { renderWithRedux } from '../../../tests_utils/ComponentsTestUtils';

describe('AddAssertionForRole', () => {
    it('should render', () => {
        const cancel = function () {};
        const domain = 'domain';
        const role = 'roleName';
        const { getByTestId } = renderWithRedux(
            <AddAssertionForRole
                cancel={cancel}
                domain={domain}
                role={role}
            />
        );
        const addPolicy = getByTestId('add-assertion-for-role');
        expect(addPolicy).toMatchSnapshot();
    });

    it('should render failed to submit action is required', () => {
        const cancel = function () {};
        const domain = 'domain';
        const role = 'roleName';
        const { getByTestId, getByText } = renderWithRedux(
            <AddAssertionForRole
                cancel={cancel}
                domain={domain}
                role={role}
            />
        );
        const addPolicy = getByTestId('add-assertion-for-role');
        expect(addPolicy).toMatchSnapshot();

        fireEvent.click(getByText('Submit'));

        expect(getByText('Rule action is required.')).not.toBeNull();
    });
});
