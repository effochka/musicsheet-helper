package com.eff.musicSheetHelper;

public enum Note {
    C8(4186.01, "C8"),
    B7(3951.07, "B7"),
    A7s(3729.31, "A7s"),
    B7f(3729.31, "B7f"),
    A7(3520.00, "A7"),
    G7s(3322.44, "G7s"),
    A7f(3322.44, "A7f"),
    G7(3135.96, "G7"),
    F7s(2959.96, "F7s"),
    G7f(2959.96, "G7f"),
    F7(2793.83, "F7"),
    E7(2637.02, "E7"),
    D7s(2489.02, "D7s"),
    E7f(2489.02, "E7f"),
    D7(2349.32, "D7"),
    C7s(2217.46, "C7s"),
    D7f(2217.46, "D7f"),
    C7(2093.00, "C7"),
    B6(1975.53, "B6"),
    A6s(1864.66, "A6s"),
    B6f(1864.66, "B6f"),
    A6(1760.00, "A6"),
    G6s(1661.22, "G6s"),
    A6f(1661.22, "A6f"),
    G6(1567.98, "G6"),
    F6s(1479.98, "F6s"),
    G6f(1479.98, "G6f"),
    F6(1396.91, "F6"),
    E6(1318.51, "E6"),
    D6s(1244.51, "D6s"),
    E6f(1244.51, "E6f"),
    D6(1174.66, "D6"),
    C6s(1108.73, "C6s"),
    D6f(1108.73, "D6f"),
    C6(1046.50, "C6"),
    B5(987.767, "B5"),
    A5s(932.328, "A5s"),
    B5f(932.328, "B5f"),
    A5(880.000, "A5"),
    G5s(830.609, "G5s"),
    A5f(830.609, "A5f"),
    G5(783.991, "G5"),
    F5s(739.989, "F5s"),
    G5f(739.989, "G5f"),
    F5(698.456, "F5"),
    E5(659.255, "E5"),
    D5s(622.254, "D5s"),
    E5f(622.254, "E5f"),
    D5(587.330, "D5"),
    C5s(554.365, "C5s"),
    D5f(554.365, "D5f"),
    C5(523.251, "C5"),
    B4(493.883, "B4"),
    A4s(466.164, "A4s"),
    B4f(466.164, "B4f"),
    A4(440.000, "A4"),
    G4s(415.305, "G4s"),
    A4f(415.305, "A4f"),
    G4(391.995, "G4"),
    F4s(369.994, "F4s"),
    G4f(369.994, "G4f"),
    F4(349.228, "F4"),
    E4(329.628, "E4"),
    D4s(311.127, "D4s"),
    E4f(311.127, "E4f"),
    D4(293.665, "D4"),
    C4s(277.183, "C4s"),
    D4f(277.183, "D4f"),
    C4(261.626, "C4"),
    B3(246.942, "B3"),
    A3s(233.082, "A3s"),
    B3f(233.082, "B3f"),
    A3(220.000, "A3"),
    G3s(207.652, "G3s"),
    A3f(207.652, "A3f"),
    G3(195.998, "G3"),
    F3s(184.997, "F3s"),
    G3f(184.997, "G3f"),
    F3(174.614, "F3"),
    E3(164.814, "E3"),
    D3s(155.563, "D3s"),
    E3f(155.563, "E3f"),
    D3(146.832, "D3"),
    C3s(138.591, "C3s"),
    D3f(138.591, "D3f"),
    C3(130.813, "C3"),
    B2(123.471, "B2"),
    A2s(116.541, "A2s"),
    B2f(116.541, "B2f"),
    A2(110.000, "A2"),
    G2s(103.826, "G2s"),
    A2f(103.826, "A2f"),
    G2(97.9989, "G2"),
    F2s(92.4986, "F2s"),
    G2f(92.4986, "G2f"),
    F2(87.3071, "F2"),
    E2(82.4069, "E2"),
    D2s(77.7817, "D2s"),
    E2f(77.7817, "E2f"),
    D2(73.4162, "D2"),
    C2s(69.2957, "C2s"),
    D2f(69.2957, "D2f"),
    C2(65.4064, "C2"),
    B1(61.7354, "B1"),
    A1s(58.2705, "A1s"),
    B1f(58.2705, "B1f"),
    A1(55.0000, "A1"),
    G1s(51.9131, "G1s"),
    A1f(51.9131, "A1f"),
    G1(48.9994, "G1"),
    F1s(46.2493, "F1s"),
    G1f(46.2493, "G1f"),
    F1(43.6535, "F1"),
    E1(41.2034, "E1"),
    D1s(38.8909, "D1s"),
    E1f(38.8909, "E1f"),
    D1(36.7081, "D1"),
    C1s(34.6478, "C1s"),
    D1f(34.6478, "D1f"),
    C1(32.7032, "C1"),
    B0(30.8677, "B0"),
    A0s(29.1352, "A0s"),
    B0f(29.1352, "B0f"),
    A0(27.5000, "A0");

    private final double mFrequency;
    private String mName;

    Note(final double frequency, final String name) {
        mFrequency = frequency;
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public static Note findClosestNote(final double frequency) {
        int low = 0;
        int high = values().length - 1;

        while (low < high) {
            int mid = (low + high) / 2;

            final double firstDifference = Math.abs(values()[mid].mFrequency - frequency);
            final double secondDifference = Math.abs(values()[mid + 1].mFrequency - frequency);

            if (secondDifference <= firstDifference) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return values()[high];
    }
}
