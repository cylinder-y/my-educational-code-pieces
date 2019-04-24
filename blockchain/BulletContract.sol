pragma solidity ^0.5.7;

/// @title Voting smart contract with option to delegate.
contract Ballot {
    // This will declare a new complex data type, which we can use to represent individual voters.
    struct ballotVoter {
        bool registered; // delegateWeight is accumulated by delegation
        bool voteSpent;  // if true, that person already used their vote
        uint voteIndex;   // index of the proposal that was voted for
    }

    // This is a type for a single proposal.
    struct Proposal {
        bytes32 proposalName;   // short name for the proposal (up to 32 bytes)
        uint voteCount; // the number of votes accumulated
    }

    address public chairman;

    // Declare state variable to store a 'ballotVoter' struct for every possible address.
    mapping(address => ballotVoter) public ballotVoters;

    // A dynamically-sized array of 'Proposal' structs.
    Proposal[] public proposalsOption;

    /// New ballot for choosing one of the 'proposalNames'
    constructor(bytes32[]memory proposalNames) public {
        chairman = msg.sender;
        ballotVoters[msg.sender].registered = true;

        // For every provided proposal names, a new proposal object is created and added to the array's end.
        for (uint i = 0; i < proposalNames.length; i++) {
            // 'Proposal({...})' will create a temporary Proposal object 
            // 'proposalsOption.push(...)' will append it to the end of 'proposalsOption'.
            proposalsOption.push(Proposal({
                proposalName: proposalNames[i] ,
                voteCount: 0
            }));
        }
    }

    // Give 'ballotVoter' the right to cast a vote on this ballot.
    // Can only be called by 'chairman'.
    function giveVotingRights(address ballotVoter) public {
        // In case the argument of 'require' is evaluted to 'false',
        // it will terminate and revert all
        // state and Ether balance changes. It is often
        // a good idea to use this in case the functions are
        // not called correctly. Keep in mind, however, that this
        // will currently also consume all of the provided gas
        // (this is planned to change in the future).
        
        require(msg.sender == chairman, "Function should be called only by chairman");
        require(!ballotVoters[ballotVoter].voteSpent, "This voter has been voted already");
        require(!ballotVoters[ballotVoter].registered, "This voter has been registered already");
        ballotVoters[ballotVoter].registered = true;
    }

    /*Voter self-registration*/
    function selfRegistration() public {
        // In case the argument of 'require' is evaluted to 'false',
        // it will terminate and revert all
        // state and Ether balance changes. It is often
        // a good idea to use this in case the functions are
        // not called correctly. Keep in mind, however, that this
        // will currently also consume all of the provided gas
        // (this is planned to change in the future).
        
        require(!ballotVoters[msg.sender].voteSpent, "This voter has been voted already");
        require(!ballotVoters[msg.sender].registered, "This voter has been registered already");
        ballotVoters[msg.sender].registered = true;
    }
    
    /// Give your vote (including votes delegated to you) to proposal 'proposalsOption[proposal].proposalName'.
    function voteIndex(uint proposal) public {
        ballotVoter storage sender = ballotVoters[msg.sender];
        require(!sender.voteSpent && sender.registered);
        sender.voteSpent = true;
        sender.voteIndex = proposal;

        // If 'proposal' is out of the range of the array,
        // this will throw automatically and revert all
        // changes.
        proposalsOption[proposal].voteCount += 1;
    }

    /// @dev Computes which proposal wins by taking all previous votes into account.
    function winnerProposal() private view
            returns (uint winnerProposal)
    {
        uint winnerVoteCount = 0;
        for (uint p = 0; p < proposalsOption.length; p++) {
            if (proposalsOption[p].voteCount > winnerVoteCount) {
                winnerVoteCount = proposalsOption[p].voteCount;
                winnerProposal = p;
            }
        }
    }

    /// Calls winnerProposal() function in order to acquire the index
    /// of the winner which the proposalsOption array contains and then
    /// returns the name of the winning proposal
    function winner() public view
            returns (bytes32 winner)
    {
        winner = proposalsOption[winnerProposal()].proposalName;
    }
}